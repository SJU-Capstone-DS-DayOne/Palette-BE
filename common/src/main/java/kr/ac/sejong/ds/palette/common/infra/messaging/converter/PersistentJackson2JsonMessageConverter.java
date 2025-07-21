package kr.ac.sejong.ds.palette.common.infra.messaging.converter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


public class PersistentJackson2JsonMessageConverter extends Jackson2JsonMessageConverter {
    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);  // deliveryMode를 PERSISTENT로 설정 -> 메시지를 항상 디스크에 저장
        return super.createMessage(object, messageProperties);
    }
}