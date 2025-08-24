package kr.ac.sejong.ds.palette.common.infra.messaging.service;

import kr.ac.sejong.ds.palette.common.infra.messaging.dto.BatchJobRequest;
import kr.ac.sejong.ds.palette.common.infra.messaging.dto.MemberInteractionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    @Value("${rabbitmq.exchanges.interaction}")
    private String interactionExchange;

    @Value("${rabbitmq.routing-keys.interaction}")
    private String interactionRoutingKey;

    @Value("${rabbitmq.exchanges.batch}")
    private String batchExchange;

    @Value("${rabbitmq.routing-keys.batch}")
    private String batchRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
    **/
    public void sendMemberInteractionMessage(MemberInteractionMessage message) {
        CorrelationData correlationData = new CorrelationData("interaction-" + UUID.randomUUID());
        rabbitTemplate.convertAndSend(interactionExchange, interactionRoutingKey, message, correlationData);
        log.info("RabbitMQ 유저 인터렉션 메시지 발행 성공 - interactionType: {}, memberId: {}, restaurantIds: {}",
                message.interactionType(), message.memberId(), message.restaurantIdList());
    }

    public void sendBatchJobRequest(String jobName, Map<String, Object> params) {
        CorrelationData correlationData = new CorrelationData("batch-" + UUID.randomUUID());
        BatchJobRequest message = new BatchJobRequest(jobName, UUID.randomUUID().toString(), params);
        rabbitTemplate.convertAndSend(batchExchange, batchRoutingKey, message, correlationData);
        log.info("배치 Job 요청 메시지 발행 성공 - jobName: {}, jobId: {}, params: {}",
                message.jobName(), message.jobId(), message.params());
    }

    public CorrelationData generateCorrelationData() {  // RabbitMQ 메시지 전송 시 고유한 CorrelationData를 생성하여 메시지 추적 가능
        return new CorrelationData(UUID.randomUUID().toString());
    }
}