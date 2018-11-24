package com.batman.mq.consumer;

import com.batman.async.EventModel;
import com.batman.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@Component
public class LikeEventConsumer implements EventConsumerInfer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_LIKE_EVENT, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel context) {

    }
}
