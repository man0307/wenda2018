package com.batman.async.handler;

import com.batman.async.EventHandler;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.model.EntityType;
import com.batman.model.HostHolder;
import com.batman.model.Message;
import com.batman.service.MassageService;
import com.batman.service.UserService;
import com.batman.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//不注册spring就无法进行类型解析
@Component
public class FollowHandler implements EventHandler {
    private List<EventType> eventTypes;
    @Autowired
    MassageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Override
    public void doHandler(EventModel eventModel) {

        Message message = new Message();
        message.setFromId(WendaUtil.getSystemUserid());
        message.setToId(eventModel.getEntityOwnerId());
        message.setConversationId(message.getConversationId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        if (eventModel.getType().equals(EventType.FOLLOW)) {
            if (eventModel.getEntityType().equals(EntityType.ENTITY_QUESTION.getCode())) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType().equals(EntityType.ENTITY_USER.getCode())) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你");
            }
        } else if (eventModel.getType().equals(EventType.UNFOLLOW)) {
            if (eventModel.getEntityType().equals(EntityType.ENTITY_QUESTION.getCode())) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "取消关注你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType().equals(EntityType.ENTITY_USER.getCode())) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "取消关注你");
            }
        }
        messageService.insert(message);

    }

    @Override
    public List<EventType> getEventHandlerSupport() {
        return Arrays.asList(EventType.FOLLOW, EventType.UNFOLLOW);
    }
}
