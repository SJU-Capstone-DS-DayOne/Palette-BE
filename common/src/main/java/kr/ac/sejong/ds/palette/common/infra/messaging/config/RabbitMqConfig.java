package kr.ac.sejong.ds.palette.common.infra.messaging.config;

import kr.ac.sejong.ds.palette.common.infra.messaging.converter.PersistentJackson2JsonMessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    // Member Interaction
    @Value("${rabbitmq.exchanges.interaction}")
    private String interactionExchange;
    @Value("${rabbitmq.queues.interaction}")
    private String interactionQueue;
    @Value("${rabbitmq.routing-keys.interaction}")
    private String interactionRoutingKey;

    // Member Embedding Status
    @Value("${rabbitmq.exchanges.embeddingStatus}")
    private String embeddingStatusExchange;
    @Value("${rabbitmq.queues.embeddingStatus}")
    private String embeddingStatusQueue;
    @Value("${rabbitmq.routing-keys.embeddingStatus}")
    private String embeddingStatusRoutingKey;

    // Batch
    @Value("${rabbitmq.exchanges.batch}")
    private String batchExchange;
    @Value("${rabbitmq.queues.batch}")
    private String batchQueue;
    @Value("${rabbitmq.routing-keys.batch}")
    private String batchRoutingKey;

    /**
     * 지정된 Exchange 이름으로 Direct Exchange Bean 을 생성 (default: durable)
     */
    @Bean
    public DirectExchange embeddingStatusExchange() {
        return new DirectExchange(embeddingStatusExchange);
    }

    @Bean
    public DirectExchange interactionExchange() {
        return new DirectExchange(interactionExchange);
    }

    @Bean
    public DirectExchange batchExchange() {
        return new DirectExchange(batchExchange);
    }

    /**
     * 지정된 큐 이름으로 Queue Bean 을 생성 (durable)
     */
    @Bean
    public Queue embeddingStatusQueue() {
        return QueueBuilder.durable(embeddingStatusQueue)
                .withArgument("x-dead-letter-exchange", "embedding-status.dlx")
                .withArgument("x-dead-letter-routing-key", "embedding-status.dlq")
                .build();
    }

    @Bean
    public Queue interactionQueue() {
        return QueueBuilder.durable(interactionQueue).build();
    }

    @Bean
    public Queue batchQueue() {
        return QueueBuilder.durable(batchQueue).build();
    }

    /**
     * 주어진 Queue 와 Exchange 을 Binding 하고 Routing Key 을 이용하여 Binding Bean 생성
     * Exchange 에 Queue 을 등록한다고 이해하자
     **/
    @Bean
    public Binding embeddingBinding() {
        return BindingBuilder
                .bind(embeddingStatusQueue())
                .to(embeddingStatusExchange())
                .with(embeddingStatusRoutingKey);
    }

    @Bean
    public Binding interactionBinding() {
        return BindingBuilder
                .bind(interactionQueue())
                .to(interactionExchange())
                .with(interactionRoutingKey);
    }

    @Bean
    public Binding batchBinding() {
        return BindingBuilder
                .bind(batchQueue())
                .to(batchExchange())
                .with(batchRoutingKey);
    }

    /**
     * RabbitMQ 연동을 위한 ConnectionFactory 빈을 생성하여 반환
     **/
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherReturns(true);  // publisher returns 활성화
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);  // publisher confirm 활성화
        return connectionFactory;
    }

    /**
     * RabbitTemplate을 생성하여 반환
     * ConnectionFactory 로 연결 후 실제 작업을 위한 Template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RetryableRabbitTemplate retryableRabbitTemplate = new RetryableRabbitTemplate(connectionFactory);  // RetryableRabbitTemplate 사용
        retryableRabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        RetryTemplate retryTemplate = getRetryTemplate();  // Retry 설정을 위한 RetryTemplate 생성
        retryableRabbitTemplate.setRetryTemplate(retryTemplate);  // RetryTemplate 적용

        return retryableRabbitTemplate;
    }

    private static RetryTemplate getRetryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();  // RetryTemplate 생성
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3);  // RetryPolicy - 최대 3회 재시도
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();  // BackOffPolicy - 1초 → 2초 → 4초 간격으로 재시도
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(4000);

        retryTemplate.setRetryPolicy(simpleRetryPolicy);  // RetryPolicy 적용
        retryTemplate.setBackOffPolicy(backOffPolicy);  // BackOffPolicy 적용
        return retryTemplate;
    }

    /**
     * Jackson 라이브러리를 사용하여 메시지를 JSON 형식으로 변환하는 MessageConverter 빈을 생성 (persistent message)
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new PersistentJackson2JsonMessageConverter();
    }
}