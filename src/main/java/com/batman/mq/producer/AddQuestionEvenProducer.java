package com.batman.mq.producer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @author manchaoyang
 * 2018/11/23
 */
@EvenTypeAnnotation(values = {EventType.ADD_QUESTION})
@Component("addQuestionEvenProducer")
public class AddQuestionEvenProducer extends AbstractEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionEvenProducer.class);

    /**
     * 因为是不是单例 所以要构造函数注入
     */
    private RabbitTemplate rabbitTemplate;
    /**
     * 构造方法注入rabbitTemplate
     *
     * @param rabbitTemplate
     */
    @Autowired
    public AddQuestionEvenProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public String getRoutingKey() {
        return  RabbitMQConfig.ROUTINGKEY_ADD_QUESTION;
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
