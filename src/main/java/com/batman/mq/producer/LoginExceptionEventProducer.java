package com.batman.mq.producer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@EvenTypeAnnotation(values = {EventType.LOGIN})
@Component("loginExceptionEventProducer")
public class LoginExceptionEventProducer extends AbstractEventProducer implements RabbitTemplate.ConfirmCallback{

    /**
     * 构造方法注入rabbitTemplate
     *
     * @param rabbitTemplate
     */
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public LoginExceptionEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public String getRoutingKey() {
        return RabbitMQConfig.ROUTINGKEY_LOGIN_EXCEPTION;
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
