package com.nowcoder.service.serviceImpl;
import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Override
    public void addUser(User u) {
         userMapper.insert(u);
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public User selectUserById(Integer id) {
        return  userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, String> register(String username, String password) {
          Map<String, String> map=new HashMap<>();
          if(StringUtils.isBlank(username)){
              map.put("msg","用户名不能为空");
              return map;
          }
         if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
         }
         User user= userMapper.selectByUsername(username);
         if(user!=null){
             map.put("msg","用户名已经存在");
             return map;
         }
         User user_=new User();
         user_.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
         user_.setName(username);
         String salt=UUID.randomUUID().toString().substring(0,5);
         //加盐 防止普通MD5被破解
         user_.setSalt(salt);
         user_.setPassword(WendaUtil.MD5(password+salt));
         userMapper.insert(user_);
         String loginTicket=addLoginTicket(userMapper.selectByUsername(user_.getName()).getId());
         map.put("ticket",loginTicket);
         return map;
    }

    @Override
    public Map<String, String> login(String username, String password) {
        Map<String, String> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码为空");
            return map;
        }
        User user= userMapper.selectByUsername(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!user.getPassword().equals(WendaUtil.MD5(password+user.getSalt()))){
            map.put("msg","密码错误");
            return map;
        }
        map.put("userId",String.valueOf(user.getId()));
        String loginTicket=addLoginTicket(userMapper.selectByUsername(username).getId());
        map.put("ticket",loginTicket);
        return map;
    }

    @Override
    public User selectByUsername(String name) {
        return userMapper.selectByUsername(name);
    }

    //cookie中的ticket串 判断登录的状态和时间
    public String addLoginTicket(Integer userId){
        LoginTicket loginTicket=new LoginTicket();
        Date limitTime=new Date();
        limitTime.setTime(limitTime.getTime()+3600*24*100);
        loginTicket.setUserId(userId);
        loginTicket.setExpired(limitTime);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketMapper.insert(loginTicket);
        return loginTicket.getTicket();
    }


}
