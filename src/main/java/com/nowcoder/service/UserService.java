package com.nowcoder.service;

import com.nowcoder.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void addUser(User u);
    List<User> selectAll();
    User selectUserById(Integer id);
    Map<String,String> register(String username,String password);
    Map<String,String> login(String username,String password);
    User selectByUsername(String name);
}
