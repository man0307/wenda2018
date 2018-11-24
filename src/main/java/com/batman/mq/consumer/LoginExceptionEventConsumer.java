package com.batman.mq.consumer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import com.batman.util.MailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@Component
public class LoginExceptionEventConsumer implements EventConsumerInfer {

    @Autowired
    MailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LOGIN_EXCEPTION, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel eventModel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", eventModel.getValue("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getValue("email"), "登陆IP异常", "mails/login_exception.html", map);
    }
}
