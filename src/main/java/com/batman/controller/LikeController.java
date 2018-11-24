package com.batman.controller;

import com.batman.async.EventModel;
import com.batman.async.EventProducer;
import com.batman.async.EventType;
import com.batman.model.Comment;
import com.batman.model.EntityType;
import com.batman.model.HostHolder;
import com.batman.model.User;
import com.batman.mq.producer.EventProducerEntrance;
import com.batman.service.CommentService;
import com.batman.service.LikeService;
import com.batman.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducerEntrance eventProducerEntrance;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/like")
    @ResponseBody
    public String like(@RequestParam(value = "commentId") Integer commentId) {
        try {
            if (hostHolder.get() == null) {
                return WendaUtil.getJSONString(999);
            }
            User user = hostHolder.get();
            Comment comment = commentService.selectByPrimaryKey(commentId);
            //添加异步的点赞发送邮件功能
            eventProducerEntrance.fireEvent(new EventModel(EventType.LIKE).
                    setActorId(hostHolder.get().getId()).setEntityId(commentId).
                    setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId()).
                    setValue("questionId", String.valueOf(comment.getEntityId())));

            Long likeCount = likeService.like(user.getId(), comment.getEntityId(), comment.getEntityType());
            return WendaUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            return WendaUtil.getJSONString(1, "未知错误");
        }
    }

    @RequestMapping(value = "/dislike")
    @ResponseBody
    public String dislike(@RequestParam(value = "commentId") Integer commentId) {
        try {
            if (hostHolder.get() == null) {
                return WendaUtil.getJSONString(999);
            }
            User user = hostHolder.get();
            Comment comment = commentService.selectByPrimaryKey(commentId);
            Long likeCount = likeService.dislike(user.getId(), comment.getEntityId(), comment.getEntityType());
            return WendaUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            return WendaUtil.getJSONString(1, "未知错误");
        }
    }
}
