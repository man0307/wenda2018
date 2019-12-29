package com.batman.model;

import org.springframework.stereotype.Component;

/**
 * @author yehuo
 * <p>
 * 使用线程本地变量来存储用户信息
 * 切记再使用之后记得clear掉 避免内存泄漏 因为其本身是弱引用
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User get() {
        return users.get();
    }

    public void set(User u) {
        users.set(u);
    }

    public void clear() {
        users.remove();
    }
}
