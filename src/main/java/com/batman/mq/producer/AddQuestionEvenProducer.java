package com.batman.mq.producer;

import com.batman.async.EventModel;
import com.batman.config.RabbitMQConfig;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * @author manchaoyang
 * 2018/11/23
 */
@Component("AddQuestionEvenProducer")
public class AddQuestionEvenProducer extends AbstractEventProducer implements RabbitTemplate.ConfirmCallback {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionEvenProducer.class);

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public AddQuestionEvenProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public void sendEvent(EventModel eventModel) {
        CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ADD_QUESTION,
                RabbitMQConfig.ROUTINGKEY_ADD_QUESTION,
                SerializationUtils.serialize(eventModel),
                correlation);
        super.addEventCache(correlation.getId(), eventModel);
    }

    @Override
    public void sendEvent(String correlationId, EventModel eventModel) {
        CorrelationData correlation = new CorrelationData(correlationId);
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ADD_QUESTION,
                RabbitMQConfig.ROUTINGKEY_ADD_QUESTION,
                SerializationUtils.serialize(eventModel),
                correlation);
    }

    /**
     * MQ的消息确认 如果ack = true 那么就将该消息的缓存删除， 如果为false 那么就重发该消息
     * 如果重发次数大于3次 那么就打上日志 将该消息的缓存删除并且不再重发
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            deleteEventCache(correlationData.getId());
            logger.info("消息成功消费");
        } else {
            EventModel event = getEventModel(correlationData.getId());
            if (null != event) {
                if (event.getResendCount() >= 3) {
                    logger.error("该消息发送失败:" + event.toString());
                } else {
                    event.setResendCount(event.getResendCount() + 1);
                    sendEvent(correlationData.getId(), event);
                }
            }
            logger.info("重新发送消息:" + correlationData.getId());
        }
    }
}
