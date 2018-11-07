package com.batman.async.handler;

import com.batman.async.EventHandler;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        // xxxx判断发现这个用户登陆异常
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getValue("username"));
        mailSender.sendWithHTMLTemplate(model.getValue("email"), "登陆IP异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getEventHandlerSupport() {
        return Arrays.asList(EventType.LOGIN);
    }
}
