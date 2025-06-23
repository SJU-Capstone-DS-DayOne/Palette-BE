package kr.ac.sejong.ds.palette.common.infra.messaging.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {

//    private final RabbitMqProperties rabbitMqProperties;

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

    /**
     * 지정된 Exchange 이름으로 Direct Exchange Bean 을 생성
     */
    @Bean
    public DirectExchange embeddingExchange() {
        return new DirectExchange(embeddingStatusExchange);
    }

    @Bean
    public DirectExchange interactionExchange() {
        return new DirectExchange(interactionExchange);
    }

    /**
     * 지정된 큐 이름으로 Queue Bean 을 생성
     */
    @Bean
    public Queue embeddingQueue() {
        return new Queue(embeddingStatusQueue);
    }

    @Bean
    public Queue interactionQueue() {
        return new Queue(interactionQueue);
    }


    /**
     * 주어진 Queue 와 Exchange 을 Binding 하고 Routing Key 을 이용하여 Binding Bean 생성
     * Exchange 에 Queue 을 등록한다고 이해하자
     **/
    @Bean
    public Binding embeddingBinding() {
        return BindingBuilder
                .bind(embeddingQueue())
                .to(embeddingExchange())
                .with(embeddingStatusRoutingKey);
    }

    @Bean
    public Binding interactionBinding() {
        return BindingBuilder
                .bind(interactionQueue())
                .to(interactionExchange())
                .with(interactionRoutingKey);
    }

    /**
     * RabbitTemplate을 생성하여 반환
     * ConnectionFactory 로 연결 후 실제 작업을 위한 Template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Jackson 라이브러리를 사용하여 메시지를 JSON 형식으로 변환하는 MessageConverter 빈을 생성
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}