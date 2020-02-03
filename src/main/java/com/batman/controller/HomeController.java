package com.batman.controller;


import com.batman.dao.QuestionMapper;
import com.batman.model.*;
import com.batman.service.CommentService;
import com.batman.service.FollowService;
import com.batman.service.QuestionService;
import com.batman.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = {"/user/{userId}"})
    public String userIndex(Model model, @PathVariable("userId") Integer userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.selectUserById(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andUserIdEqualTo(userId);
        vo.set("commentCount", commentService.countByExample(commentExample));
        vo.set("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER.getCode()));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER.getCode()));
        if (hostHolder.get() != null) {
            vo.set("followed", followService.isFollower(hostHolder.get().getId(), userId, EntityType.ENTITY_USER.getCode()));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {

        model.addAttribute("vos", getQuestions(null, 0, 10));
        model.addAttribute("limit", 10);
        return "index";
    }

    @RequestMapping(value = {"/moreQuestion/{limit}"})
    public String moreQuestion(Model model, @PathVariable("limit") int limit) {
        model.addAttribute("vos", getQuestions(null, 0, limit + 10));
        model.addAttribute("limit", limit + 10);
        return "index";
    }

    private List<ViewObject> getQuestions(Integer userId, Integer offset, Integer limit) {
        //mybatis分页算法
        List<Question> list = questionService.selectByLimit(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question value : list) {
            ViewObject vo = new ViewObject();
            //如果描述过长 使列表中的问题描述不全部展开
            if (value.getContent().length() > 200) {
                value.setContent(value.getContent().trim().substring(0, 200));
            }
            vo.set("question", value);
            User user = userService.selectUserById(value.getUserId());
            vo.set("followCount", followService.getFollowerCount(value.getId(), EntityType.ENTITY_QUESTION.getCode()));
            vo.set("user", user);
            vos.add(vo);
        }
        return vos;
    }

}
