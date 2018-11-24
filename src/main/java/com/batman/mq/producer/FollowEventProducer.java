package com.batman.mq.producer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@EvenTypeAnnotation(values = {EventType.FOLLOW})
@Component("followEventProducer")
public class FollowEventProducer extends AbstractEventProducer implements RabbitTemplate.ConfirmCallback{
    private final Logger logger = LoggerFactory.getLogger(FollowEventProducer.class);

    /**
     * 构造方法注入rabbitTemplate
     *
     * @param rabbitTemplate
     */
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public FollowEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public String getRoutingKey() {
        return RabbitMQConfig.ROUTINGKEY_FOLLOW_EVENT;
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
