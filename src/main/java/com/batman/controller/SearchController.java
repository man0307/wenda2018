package com.batman.controller;

import com.batman.model.EntityType;
import com.batman.model.Question;
import com.batman.model.User;
import com.batman.model.ViewObject;
import com.batman.mq.producer.EventProducerEntrance;
import com.batman.service.FollowService;
import com.batman.service.QuestionService;
import com.batman.service.SeacherService;
import com.batman.service.UserService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    QuestionService questionService;

    @Autowired
    SeacherService seacherService;

    @Autowired
    FollowService followService;


    @Autowired
    UserService userService;

    @Autowired
    EventProducerEntrance eventProducerEntrance;

    //使用solr的Search方法 因为每次都要开启solr很不方便 所以增添一个使用like的方法
    @RequestMapping(value = "/search_solr")
    //展示对话详情  标识为未读的消息变成已读消息
    public String search_solr(Model model, @RequestParam(value = "q") String keyword,
                              @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                              @RequestParam(value = "count", defaultValue = "10") Integer count) throws IOException, SolrServerException {
        List<Question> list = seacherService.searchQuestion(keyword, offset, count, "<em>", "</em>");
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : list) {
            try {
                ViewObject vo = new ViewObject();
                Question q = questionService.selectByQuestionId(question.getId());
                //如果描述过长 使列表中的问题描述不全部展开
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (q.getContent().length() > 200) {
                    q.setContent(question.getContent().trim().substring(0, 200));
                }
                vo.set("question", q);
                User user = userService.selectUserById(q.getUserId());
                vo.set("followCount", followService.getFollowerCount(q.getId(), EntityType.ENTITY_QUESTION.getCode()));
                vo.set("user", user);
                vos.add(vo);
            }catch (Exception e){
                //todo logger
            }
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("vos", vos);
        return "result";
    }

    @RequestMapping(value = "/search")
    //展示对话详情  标识为未读的消息变成已读消息
    public String search(Model model, @RequestParam(value = "q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                         @RequestParam(value = "count", defaultValue = "10") Integer count) throws IOException, SolrServerException {
        List<Question> list = questionService.getQuestionsByKeyword(keyword);

        List<ViewObject> vos = new ArrayList<>();
        for (Question question : list) {
            ViewObject vo = new ViewObject();

            if (question.getContent().length() > 200) {
                question.setContent(question.getContent().trim().substring(0, 200));
            }
            vo.set("question", question);
            User user = userService.selectUserById(question.getUserId());
            vo.set("followCount", followService.getFollowerCount(question.getId(), EntityType.ENTITY_QUESTION.getCode()));
            vo.set("user", user);
            vos.add(vo);
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("vos", vos);
        return "result";
    }
}
