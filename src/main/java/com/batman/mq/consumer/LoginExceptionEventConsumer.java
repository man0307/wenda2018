package com.batman.mq.consumer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@EvenTypeAnnotation(values = {EventType.LOGIN})
@Component
public class LoginExceptionEventConsumer implements EventConsumerInfer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LOGIN_EXCEPTION, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel context) {

    }
}
