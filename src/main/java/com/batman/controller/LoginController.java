package com.batman.controller;

import com.batman.model.HostHolder;
import com.batman.mq.producer.EventProducerEntrance;
import com.batman.service.TicketService;
import com.batman.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TicketService ticketService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducerEntrance eventProducerEntrance;

    @RequestMapping(value = {"/reg"})
    public String register(Model model,
                           @RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           HttpServletResponse response, @RequestParam(value = "rememberme") Boolean rememberme,
                           @RequestParam(value = "next") String next) {
        Map<String, String> map = userService.register(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    //设置持久化cookie
                    cookie.setMaxAge(3600 * 24 * 50);
                }
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            map.put("msg", "登录异常");
            return "login";
        }
    }


    @RequestMapping(value = {"/login"})
    public String userLogin(Model model,
                            @RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password,
                            HttpServletResponse response, @RequestParam(value = "rememberme") Boolean rememberme,
                            @RequestParam(value = "next") String next) {

        Map<String, String> map = userService.login(username, password);
//        eventProducerEntrance.fireEvent(new EventModel(EventType.LOGIN).setValue("username",username).setValue("email","530231559@qq.com")
//                .setActorId(Integer.valueOf(map.get("userId"))));

        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    //设置持久化cookie
                    cookie.setMaxAge(3600 * 24 * 10);
                }
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            map.put("msg", "注册异常");
            return "login";
        }

    }


    ///logout 退出登录 思想 将ticket的状态改为1 表示非登录状态
    @RequestMapping(value = {"/logout"})
    public String logout(@CookieValue("ticket") String ticket) {
        ticketService.updateTicketStatusByTicket(ticket, 1);
        return "login";
    }

    @RequestMapping(value = {"/reglogin"})
    public String userIndex(
            Model model,
            @RequestParam(value = "next", required = false) String next
    ) {

        model.addAttribute("next", next);
        return "login";
    }
}
