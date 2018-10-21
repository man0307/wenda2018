package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class AddQuestionHandler implements EventHandler {
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
    SeacherService seacherService;


    @Override
    public void doHandler(EventModel eventModel) {
        try {
           System.out.println(seacherService.indexQuestion(eventModel.getEntityId()
                   , eventModel.getValue("title"), eventModel.getValue("content")));
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<EventType> getEventHandlerSupport() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
