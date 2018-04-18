package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam(value = "title") String title,
                              @RequestParam(value = "content") String content) {
        Question question = new Question();
        if (hostHolder.get() == null) return WendaUtil.getJSONString(999);
        else question.setUserId(hostHolder.get().getId());
        question.setTitle(title);
        question.setContent(content);
        question.setCreatedDate(new Date());
        question.setCommentCount(0);

        int code = questionService.addQuestion(question);
        //0代表添加成功
        if (code > 0) {
            eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION).
                    setActorId(question.getUserId()).setEntityId(question.getId()).setEntityType(EntityType.ENTITY_QUESTION)
            .setValue("title",question.getTitle()).setValue("content",question.getContent()));
            return WendaUtil.getJSONString(0);
        }
        //1代表添加失败
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String addQuestion(Model model, @PathVariable(value = "qid") Integer qid) {
        Question question = questionService.selectByQuestionId(qid);
        model.addAttribute("question", question);
        User user = userService.selectUserById(question.getUserId());
        model.addAttribute("user", user);
        if (hostHolder.get() != null)
            model.addAttribute("followed", followService.isFollower(hostHolder.get().getId(), qid, EntityType.ENTITY_QUESTION));

        //得到followUsers
        Integer len = Integer.valueOf(String.valueOf(followService.getFollowerCount(qid, EntityType.ENTITY_QUESTION)));
        List<Integer> ids = followService.getFollowers(qid, EntityType.ENTITY_QUESTION, 0, len);
        List<User> followUsers = new ArrayList<>();
        for (int id : ids) {
            User u = userService.selectUserById(id);
            if (u != null) followUsers.add(u);
        }

        model.addAttribute("followUsers", followUsers);

        List<Comment> commentList = commentService.selectByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            ViewObject vo = new ViewObject();
            Comment comment = commentList.get(i);
            vo.set("comment", commentList.get(i));
            vo.set("user", userService.selectUserById(comment.getUserId()));
            if (hostHolder.get() == null) {
                vo.set("liked", "0");
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.get().getId(), comment.getEntityId(), comment.getEntityType()));
            }
            vo.set("likeCount", likeService.getLikeCount(comment.getEntityId(), comment.getEntityType()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }

}
