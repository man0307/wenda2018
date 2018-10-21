package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//不注册spring就无法进行类型解析
@Component
public class FeedHandler implements EventHandler {
    private List<EventType> eventTypes;
    @Autowired
    CommentService commentService;

    @Autowired
    MassageService messageService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FeedService feedService;

    @Autowired
    QuestionService questionService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel eventModel) {
        Map<String, String> info = new HashMap<>();
        User user = userService.selectUserById(eventModel.getActorId());
        if (user == null) {
            return null;
        }
        info.put("UserId", String.valueOf(user.getId()));
        info.put("UserHead", user.getHeadUrl());
        info.put("UserName", user.getName());
        if (eventModel.getType() == EventType.COMMENT || (eventModel.getType() == EventType.FOLLOW
                && eventModel.getEntityType() == EntityType.ENTITY_QUESTION)) {
            if (eventModel.getType() == EventType.COMMENT) {
                Comment comment = commentService.selectByPrimaryKey(eventModel.getEntityId());
                if (comment == null) {
                    return null;
                }
                Question question = questionService.selectByQuestionId(eventModel.getEntityOwnerId());
                info.put("questionTitle", question.getTitle());
                info.put("questionId", question.getTitle());
                info.put("content", comment.getContent());

            } else {
                Question question = questionService.selectByQuestionId(eventModel.getEntityId());
                if (question == null) {
                    return null;
                }
                info.put("questionId", String.valueOf(question.getId()));
                info.put("questionTitle", question.getTitle());
            }

        }
        return JSONObject.toJSON(info).toString();
    }

    @Override
    public void doHandler(EventModel eventModel) {
        Feed feed = new Feed();
        feed.setData(buildFeedData(eventModel));
        if (feed.getData() == null) {
            return;
        }
        feed.setCreatedDate(new Date());
        feed.setType(eventModel.getType().getValue());
        feed.setUserId(eventModel.getActorId());
        feedService.addFeed(feed);
        doPush(feed, eventModel);
    }

    //timeLine的推模式 根据自己的粉丝 将自己触发的新鲜事通过Redis的list异步队列将事件推出  当用户登录的时候再从队列中读取
    //提高  什么样的用户用推 什么样的用户用拉  完善一下这个功能
    public void doPush(Feed feed, EventModel eventModel) {
        List<Integer> followers = followService.getFollowers(eventModel.getActorId(), EntityType.ENTITY_USER, 10);
        //如果没有粉丝则不推送
        if (followers == null) {
            return;
        }
        for (Integer id : followers) {
            User user = userService.selectUserById(id);
            if (user == null) {
                continue;
            }
            String timelineKey = JedisKeyUtil.getTimelineKey(user.getId());
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getEventHandlerSupport() {
        return Arrays.asList(EventType.FOLLOW, EventType.COMMENT);
    }
}
