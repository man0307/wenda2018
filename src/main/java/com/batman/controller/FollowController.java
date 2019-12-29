package com.batman.controller;

import com.batman.async.EventModel;
import com.batman.async.EventProducer;
import com.batman.async.EventType;
import com.batman.model.*;
import com.batman.mq.producer.EventProducerEntrance;
import com.batman.service.*;
import com.batman.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducerEntrance eventProducerEntrance;

    @Autowired
    CommentService commentService;
    @Autowired
    FollowService followService;

    //关注用户
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            //未登录不能进行关
            return WendaUtil.getJSONString(999);
        }
        //进行关注 成功了就调用异步队列来发送站内信
        User user = hostHolder.get();
        if (followService.follow(user.getId(), userId, EntityType.ENTITY_USER.getCode())) {
            //关注成功 异步队列发送邮件
            eventProducerEntrance.fireEvent(new EventModel(EventType.FOLLOW).setActorId(user.getId())
                    .setEntityId(userId).setEntityType(EntityType.ENTITY_USER.getCode()).setEntityOwnerId(userId));
            //JSON返回当前用户关注的所有人数
            return WendaUtil.getJSONString(0, String.valueOf(followService.getFolloweeCount(user.getId(), EntityType.ENTITY_USER.getCode())));
        }
        return WendaUtil.getJSONString(1, "关注失败");
    }


    @RequestMapping(path = {"/unfollowUser"})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            //未登录不能进行关
            return WendaUtil.getJSONString(999);
        }
        //进行关注 成功了就调用异步队列来发送站内信
        User user = hostHolder.get();
        if (followService.unFollow(user.getId(), userId, EntityType.ENTITY_USER.getCode())) {
            //关注成功 异步队列发送邮件
            eventProducerEntrance.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(user.getId())
                    .setEntityId(userId).setEntityType(EntityType.ENTITY_USER.getCode()).setEntityOwnerId(userId));
            //JSON返回当前用户关注的所有人数
            return WendaUtil.getJSONString(0, String.valueOf(followService.getFolloweeCount(user.getId(), EntityType.ENTITY_USER.getCode())));
        }
        return WendaUtil.getJSONString(1, "取消关注失败");
    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {

        if (hostHolder.get() == null) {
            //未登录不能进行关
            return WendaUtil.getJSONString(999);
        }
        //进行关注 成功了就调用异步队列来发送站内信
        User user = hostHolder.get();
        Question question = questionService.selectByQuestionId(questionId);
        if (question == null) {
            return WendaUtil.getJSONString(1, "没有该问题，关注失败");
        }

        Boolean ret = followService.follow(user.getId(), questionId, EntityType.ENTITY_QUESTION.getCode());
        //关注成功 异步队列发送邮件
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", user.getHeadUrl());
        info.put("name", user.getName());
        info.put("id", user.getId());
        info.put("count", followService.getFollowerCount(questionId, EntityType.ENTITY_QUESTION.getCode()));

        eventProducerEntrance.fireEvent(new EventModel(EventType.FOLLOW).setActorId(user.getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION.getCode()).setEntityOwnerId(questionId));

        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }


    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.get() == null) {
            //未登录不能进行关
            return WendaUtil.getJSONString(999);
        }
        //进行关注 成功了就调用异步队列来发送站内信
        User user = hostHolder.get();
        Question question = questionService.selectByQuestionId(questionId);
        if (question == null) {
            return WendaUtil.getJSONString(1, "没有该问题，取消关注失败");
        }


        if (followService.unFollow(user.getId(), questionId, EntityType.ENTITY_QUESTION.getCode())) {
            Map<String, Object> info = new HashMap<>();
            info.put("headUrl", user.getHeadUrl());
            info.put("name", user.getName());
            info.put("id", user.getId());
            info.put("count", followService.getFollowerCount(questionId, EntityType.ENTITY_QUESTION.getCode()));
            //关注成功 异步队列发送邮件
            eventProducerEntrance.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(user.getId())
                    .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION.getCode()).setEntityOwnerId(questionId));
            return WendaUtil.getJSONString(0, info);
        }
        return WendaUtil.getJSONString(1);
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> ids = followService.getFollowers(userId, EntityType.ENTITY_USER.getCode(), 10);
        User user = hostHolder.get();
        if (user != null) {
            model.addAttribute("followers", getUsersInfo(user.getId(), ids));
        } else {
            model.addAttribute("followers", getUsersInfo(0, ids));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER.getCode()));
        model.addAttribute("curUser", userService.selectUserById(userId));
        return "followers";
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> ids = followService.getFollowers(userId, EntityType.ENTITY_USER.getCode(), 10);
        User user = hostHolder.get();
        if (user != null) {
            model.addAttribute("followees", getUsersInfo(user.getId(), ids));
        } else {
            model.addAttribute("followees", getUsersInfo(0, ids));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER.getCode()));
        model.addAttribute("curUser", userService.selectUserById(userId));
        return "followees";
    }

    //根据用户的id列表得到展示到前台所需要的信息
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer id : userIds) {
            User user = userService.selectUserById(id);
            //不存在这个用户的id就跳过
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            CommentExample commentExample = new CommentExample();
            CommentExample.Criteria criteria = commentExample.createCriteria();
            criteria.andUserIdEqualTo(id);
            vo.set("commentCount", commentService.countByExample(commentExample));
            vo.set("followerCount", followService.getFollowerCount(id, EntityType.ENTITY_USER.getCode()));
            vo.set("followeeCount", followService.getFolloweeCount(id, EntityType.ENTITY_USER.getCode()));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, id, EntityType.ENTITY_USER.getCode()));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
