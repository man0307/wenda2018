package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.service.MassageService;
import com.nowcoder.service.UserService;
import com.nowcoder.service.serviceImpl.MessageServiceImpl;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//不注册spring就无法进行类型解析
@Component
public class LikeHandler implements EventHandler {
    private List<EventType> eventTypes;
    @Autowired
    MassageService messageService;

    @Autowired
    UserService userService;

    //为什么此处的hostHolder为空值？？？奥艹 我明白了 因为点赞的时候是异步事件没经过拦截器
    @Autowired
    HostHolder hostHolder;

    @Override
    public void doHandler(EventModel eventModel) {
        //异步发送站内信 恭喜xxx给你点赞了 详情请见http://localhost:8080/question/questionId
        Message message = new Message();
        message.setFromId(WendaUtil.getSystemUserid());
        message.setToId(eventModel.getEntityOwnerId());
        message.setConversationId(message.getConversationId());
        message.setHasRead(0);
        message.setCreatedDate(new Date());
//        if(hostHolder.get()==null) System.out.println("fuck");
        message.setContent("恭喜" + userService.selectUserById(eventModel.getActorId()).getName() +
                "给你点赞了/n详情请见http://localhost:8080/question/" + eventModel.getValue("questionId"));
        messageService.insert(message);
    }

    @Override
    public List<EventType> getEventHandlerSupport() {
        return Arrays.asList(EventType.LIKE);
    }
}
