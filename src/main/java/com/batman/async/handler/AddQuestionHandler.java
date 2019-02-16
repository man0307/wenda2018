package com.batman.async.handler;

import com.batman.async.EventHandler;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.model.*;
import com.batman.service.*;
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
           seacherService.indexQuestion(eventModel.getEntityId()
                   , eventModel.getValue("title"), eventModel.getValue("content"));
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
