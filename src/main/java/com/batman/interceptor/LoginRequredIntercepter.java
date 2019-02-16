package com.batman.interceptor;

import com.batman.dao.LoginTicketMapper;
import com.batman.dao.UserMapper;
import com.batman.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequredIntercepter implements HandlerInterceptor {
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hostHolder.get() == null) {
            response.sendRedirect("/regLogin?next=" + request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
