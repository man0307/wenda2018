package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/addComment")
    public String addComment(Model model, @RequestParam(value = "questionId") Integer questionId,
                             @RequestParam(value = "content") String content) {
        //未登陆不能添加评论
        if (hostHolder.get() == null) {
            return "login";
        }
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setEntityId(questionId);
        comment.setCreatedDate(new Date());
        comment.setEntityType(EntityType.ENTITY_QUESTION);
        comment.setUserId(hostHolder.get().getId());
        commentService.insert(comment);

        //增加评论后要更新 评论数
        Question question = questionService.selectByQuestionId(questionId);
        //example的用法 根据commentExample来查询指定EntityId的评论
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria(); //构造自定义查询条件
        criteria.andEntityIdEqualTo(questionId);
        criteria.andEntityTypeEqualTo(EntityType.ENTITY_QUESTION);
        question.setCommentCount(commentService.countByExample(commentExample));

        eventProducer.fireEvent(new EventModel(EventType.COMMENT).
                setActorId(hostHolder.get().getId()).
                setEntityType(EntityType.ENTITY_COMMENT).setEntityId(comment.getId()).setEntityOwnerId(question.getId()));
        //产生一步事件 推拉模式生成timeline
        questionService.updateByPrimaryKeyWithBLOBs(question);
        return "redirect:/question/" + questionId;
    }
}
