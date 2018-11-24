package com.batman.mq.consumer;

import com.batman.async.EventModel;
import com.batman.config.RabbitMQConfig;
import com.batman.model.HostHolder;
import com.batman.model.Message;
import com.batman.service.MassageService;
import com.batman.service.UserService;
import com.batman.util.WendaUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author manchaoyang
 * 2018/11/24
 **/
@Component
public class LikeEventConsumer implements EventConsumerInfer {

    @Autowired
    MassageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_LIKE_EVENT, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.getSystemUserid());
        message.setToId(eventModel.getEntityOwnerId());
        message.setConversationId(message.getConversationId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        message.setContent("恭喜" + userService.selectUserById(eventModel.getActorId()).getName() +
                "给你点赞了/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
        messageService.insert(message);
    }
}
