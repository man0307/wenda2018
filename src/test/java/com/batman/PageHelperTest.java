package com.batman;

import com.batman.model.User;
import com.batman.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class PageHelperTest {
    @Autowired
    UserService userService;

    @Test
    public void startPageTest(){
        PageHelper.startPage(1,5);
        List<User> users = userService.selectAll();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        int i=0;
    }
}
