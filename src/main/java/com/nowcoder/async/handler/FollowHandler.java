package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.service.MassageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
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
        if (eventModel.getType() == EventType.FOLLOW) {
            if (eventModel.getEntityType() == EntityType.ENTITY_QUESTION) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType() == EntityType.ENTITY_USER) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "关注了你");
            }
        }else if(eventModel.getType() == EventType.UNFOLLOW){
            if (eventModel.getEntityType() == EntityType.ENTITY_QUESTION) {
                message.setContent(userService.selectUserById(eventModel.getActorId()).getName() +
                        "取消关注你的问题/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
            } else if (eventModel.getEntityType() == EntityType.ENTITY_USER) {
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
