package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.User;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    EventProducer eventProducer;

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
            eventProducer.fireEvent(new EventModel(EventType.LIKE).
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
