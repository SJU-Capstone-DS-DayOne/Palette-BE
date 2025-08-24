package kr.ac.sejong.ds.palette.common.infra.messaging.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

/*
 * RabbitTemplate을 상속받아 ReturnsCallback과 ConfirmCallback 시, 재시도 로직을 추가한 클래스
 */

@Slf4j
public class RetryableRabbitTemplate extends RabbitTemplate {

    private static final int MAX_RETRY_ATTEMPTS = 3;  // 최대 재시도 횟수

    public RetryableRabbitTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
        super.setMandatory(true);  // 메시지가 라우팅되지 못할 경우 Return Callback 호출
        initCallbacks();  // retry 로직이 적용된 ReturnsCallback과 ConfirmCallback 설정
    }

    private void initCallbacks() {

        super.setReturnsCallback(returned -> {
            log.error("메시지 라우팅 실패 - replyCode: {}, replyText: {}, exchange: {}, routingKey: {}",
                    returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
            Message message = returned.getMessage();
            MessageProperties props = message.getMessageProperties();
            Map<String, Object> headers = props.getHeaders();

            int retryCount = (int) headers.getOrDefault("x-retry-count", 0);

            if (retryCount >= MAX_RETRY_ATTEMPTS) {  // 최대 재시도 횟수 초과
                log.error("최대 재시도 횟수 초과");
                return;
            }

            Message newMessage = MessageBuilder
                    .withBody(message.getBody())
                    .copyHeaders(headers)
                    .setHeader("x-retry-count", retryCount + 1)
                    .build();

            log.warn("재시도 진행 (count: {})", retryCount + 1);
            super.convertAndSend(returned.getExchange(), returned.getRoutingKey(), newMessage);
        });

        super.setConfirmCallback((correlationData, ack, cause) -> {  // ConfirmCallback: NACK 시 재시도
            if (!ack && correlationData != null && correlationData.getReturned() != null) {
                log.error("브로커 NACK - cause: {}", cause);

                Message message = correlationData.getReturned().getMessage();
                MessageProperties props = message.getMessageProperties();
                Map<String, Object> headers = props.getHeaders();

                int retryCount = (int) headers.getOrDefault("x-retry-count", 0);

                if (retryCount >= MAX_RETRY_ATTEMPTS) {
                    log.error("최대 재시도 횟수 초과");
                    return;
                }

                Message newMessage = MessageBuilder
                        .withBody(message.getBody())
                        .copyHeaders(headers)
                        .setHeader("x-retry-count", retryCount + 1)
                        .build();

                log.warn("재시도 진행 (count: {})", retryCount + 1);
                super.convertAndSend(correlationData.getReturned().getExchange(), correlationData.getReturned().getRoutingKey(), newMessage);
            }
        });
    }
}