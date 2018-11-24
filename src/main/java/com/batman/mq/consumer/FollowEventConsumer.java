package com.batman.mq.consumer;

import com.batman.async.EventModel;
import com.batman.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FollowEventConsumer implements EventConsumerInfer {


    @RabbitListener(queues = RabbitMQConfig.QUEUE_FOLLOW_EVENT, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel context) {

    }
}
