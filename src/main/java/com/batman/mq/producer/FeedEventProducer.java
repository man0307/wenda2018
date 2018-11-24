package com.batman.mq.producer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@EvenTypeAnnotation(values = {EventType.FOLLOW, EventType.COMMENT})
@Component("feedEventProducer")
public class FeedEventProducer extends AbstractEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(FeedEventProducer.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public FeedEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public String getRoutingKey() {
        return RabbitMQConfig.ROUTINGKEY_FEED_EVENT;
    }

    @Override
    public String getExchangeName() {
        return RabbitMQConfig.EXCHANGE_ALL_EVENT;
    }

    @Override
    public RabbitTemplate getRabbitTemplate() {
        return this.rabbitTemplate;
    }

}
