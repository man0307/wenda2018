package com.nowcoder.controller;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
        vo.set("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.get() != null) {
            vo.set("followed", followService.isFollower(hostHolder.get().getId(), userId, EntityType.ENTITY_USER));
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
        for (int i = 0; i < list.size(); i++) {
            ViewObject vo = new ViewObject();
            Question question = list.get(i);
            //如果描述过长 使列表中的问题描述不全部展开
            if (question.getContent().length() > 200)
                question.setContent(question.getContent().trim().substring(0, 200));
            vo.set("question", question);
            User user = userService.selectUserById(list.get(i).getUserId());
            vo.set("followCount", followService.getFollowerCount(list.get(i).getId(), EntityType.ENTITY_QUESTION));
            vo.set("user", user);
            vos.add(vo);
        }
        return vos;
    }


//    @RequestMapping(value = "/createDate")
//    public  @ResponseBody  String  contextLoads() {
//        Random random = new Random();
//        for (int i = 1; i < 12; ++i) {
//            User user = new User();
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            user.setName(String.format("user%d", i));
//            user.setSalt(UUID.randomUUID().toString().replaceAll("-","").substring(0,5));
//            user.setPassword("123");
//            userService.addUser(user);
//            Question question = new Question();
//            question.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            question.setCreatedDate(date);
//            question.setUserId(i);
//            question.setTitle(String.format("题目{%d}", i));
//            question.setContent(String.format("Balaababalalalal 简介 %d", i));
//            questionMapper.insert(question);
//            System.out.println("插入"+user+" "+question);
//        }
//        return "sucess";
//    }
}
