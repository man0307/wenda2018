package com.batman.controller;

import com.batman.model.EntityType;
import com.batman.model.Feed;
import com.batman.model.HostHolder;
import com.batman.service.*;
import com.batman.util.JedisAdapter;
import com.batman.util.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/pushfeeds"})
    public String getPushFeeds(Model model) {
        List<Integer> feedIds = new ArrayList<>();
        List<Feed> feeds = new ArrayList<>();
        if (hostHolder.get() != null) {
            List<String> ids = jedisAdapter.lrange(JedisKeyUtil.getTimelineKey(hostHolder.get().getId()), 0, 10);
            for (String id : ids) {
                Feed feed = feedService.selectByPrimaryKey(Integer.valueOf(id));
                if (feed != null) {
                    feeds.add(feed);
                }
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }


    @RequestMapping(path = {"/pullfeeds"})
    public String getPullFeeds(Model model) {
        Integer localUserId = hostHolder.get() == null ? 0 : hostHolder.get().getId();
        List<Integer> ids = new ArrayList<>();
        if (localUserId != 0) {
            ids = followService.getFollowees(localUserId, EntityType.ENTITY_USER.getCode(), Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.selectUserFeeds(Integer.MAX_VALUE, ids, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
