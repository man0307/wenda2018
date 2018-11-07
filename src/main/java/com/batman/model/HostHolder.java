package com.batman.model;

import org.springframework.stereotype.Component;

//用来存储登录的用户
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<>();
    public User get(){
        return users.get();
    }
    public void set(User u){
        users.set(u);
    }
    public void clear(){
        users.remove();
    }
}
