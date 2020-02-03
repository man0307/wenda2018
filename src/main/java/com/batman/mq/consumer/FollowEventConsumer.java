package com.batman.mq.consumer;

import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import com.batman.model.EntityType;
import com.batman.model.HostHolder;
import com.batman.model.Message;
import com.batman.service.MassageService;
import com.batman.service.UserService;
import com.batman.util.WendaUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author yehuo
 */
@Component
public class FollowEventConsumer implements EventConsumerInfer {
    @Autowired
    MassageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_FOLLOW_EVENT, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(WendaUtil.getSystemUserid());
        message.setToId(eventModel.getEntityOwnerId());
        message.setConversationId(message.getConversationId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        if (eventModel.getType() == EventType.FOLLOW) {
            if (eventModel.getEntityType().equals(EntityType.ENTITY_QUESTION)) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType().equals(EntityType.ENTITY_USER)) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你");
            }
        } else if (eventModel.getType() == EventType.UNFOLLOW) {
            if (eventModel.getEntityType().equals(EntityType.ENTITY_QUESTION)) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "取消关注你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType().equals(EntityType.ENTITY_USER)) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "取消关注你");
            }
        }
        messageService.insert(message);
    }
}
