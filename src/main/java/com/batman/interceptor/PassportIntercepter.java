package com.batman.interceptor;

import com.batman.dao.LoginTicketMapper;
import com.batman.dao.UserMapper;
import com.batman.model.HostHolder;
import com.batman.model.LoginTicket;
import com.batman.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportIntercepter implements HandlerInterceptor {
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        LoginTicket loginTicket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket == null) {
            return true;
        } else {
            loginTicket = loginTicketMapper.selectByTicket(ticket);
        }
        if (loginTicket == null) {
            return true;
        }
        if (loginTicket.getStatus() != 0 || loginTicket.getExpired().before(new Date())) {
            return true;
        }
        User user = userMapper.selectByPrimaryKey(loginTicket.getUserId());
        hostHolder.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //页面渲染之前加上user
        if (modelAndView != null) {
            modelAndView.addObject("user", hostHolder.get());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //页面渲染之后去掉user
        hostHolder.clear();
    }
}
