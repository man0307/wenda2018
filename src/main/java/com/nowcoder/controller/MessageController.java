package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.MassageService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;

    @Autowired
    MassageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/msg/addMessage")
    @ResponseBody
    public String addQuestion(@RequestParam(value = "toName") String toName,
                              @RequestParam(value = "content") String content) {
        //验证是否登陆 未登录需要跳转到登录页
        if (hostHolder.get() == null) {
            //未登录标志999
            return WendaUtil.getJSONString(999);
        }
        User user = userService.selectByUsername(toName);
        if (user == null) {
            return WendaUtil.getJSONString(1, "要发送的用户不存在");
        }
        Message message = new Message();
        message.setContent(content);
        message.setToId(user.getId());
        message.setFromId(hostHolder.get().getId());
        message.setCreatedDate(new Date());
        message.setConversationId(message.getConversationId());
        message.setHasRead(0);
        messageService.insert(message);
        return WendaUtil.getJSONString(0);
    }

    @RequestMapping(value = "/msg/detail")
    //展示对话详情  标识为未读的消息变成已读消息
    public String messageDetail(Model model, @RequestParam(value = "conversationId") String conversationId) {
        List<Message> messageList = messageService.getMessageByLimit(conversationId, 0, 10);
        List<ViewObject> messages = new ArrayList<>();
        for (int i = 0; i < messageList.size(); i++) {
            ViewObject vo = new ViewObject();
            Message message=messageList.get(i);
            //既然显示到了详情页 那么肯定就会变成已经读过的消息
            if(message.getHasRead()==0){
                message.setHasRead(1);
                messageService.updateByPrimaryKey(message);
            }
            vo.set("message", message);
            vo.set("user", userService.selectUserById(messageList.get(i).getFromId()));
            messages.add(vo);
        }
        model.addAttribute("messages", messages);
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/list")
    public String addQuestion(Model model) {
        if(hostHolder.get()==null) return "redirect:/regLogin";
        User user=hostHolder.get();
        List<Message> messageList=messageService.getConversationList(user.getId(),0,10);
        List<ViewObject> conversations=new ArrayList<>();
        for(int i=0;i<messageList.size();i++){
            Message message=messageList.get(i);
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            vo.set("user",userService.selectUserById(message.getFromId()==user.getId()?message.getToId():message.getFromId()));
            vo.set("unread",messageService.getConversationUnreadCount(user.getId(),message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }
}
