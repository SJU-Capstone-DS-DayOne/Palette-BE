package kr.ac.sejong.ds.palette.common.infra.messaging.service;

import kr.ac.sejong.ds.palette.common.infra.messaging.dto.MemberInteractionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

    @Value("${rabbitmq.exchanges.interaction}")
    private String interactionExchange;

    @Value("${rabbitmq.routing-keys.interaction}")
    private String interactionRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
    **/
    public void sendMemberInteractionMessage(MemberInteractionMessage message) {
        rabbitTemplate.convertAndSend(interactionExchange, interactionRoutingKey, message);
        log.info("RabbitMQ 유저 인터렉션 메시지 발행 성공 - interactionType: {}, memberId: {}, restaurantIds: {}",
                message.interactionType(), message.memberId(), message.restaurantIdList());
    }
}