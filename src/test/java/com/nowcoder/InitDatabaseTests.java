package com.nowcoder;


import com.nowcoder.dao.UserMapper;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {


    @Test
    public void findBoxDetailByIdTest() throws IOException {
        String resource="mybatis-config.xml";
        try (InputStream in = Resources.getResourceAsStream(resource)) {
            //创建SqlSessionFactory
            SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(in);
            //创建sqlSession
            SqlSession sqlSession=sqlSessionFactory.openSession();
            //执行sql语句
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        } catch (IOException e) {

        }
    }

    @Test
    public void contextLoads() {
//        Random random = new Random();
//        for (int i = 0; i < 11; ++i) {
//            User user = new User();
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            user.setName(String.format("USER%d", i));
//            user.setPassword("");
//            user.setSalt("");
//            userDAO.addUser(user);
//
//            user.setPassword("newpassword");
//            userDAO.updatePassword(user);
//
//            Question question = new Question();
//            question.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            question.setCreatedDate(date);
//            question.setUserId(i + 1);
//            question.setTitle(String.format("TITLE{%d}", i));
//            question.setContent(String.format("Balaababalalalal Content %d", i));
//            questionDAO.addQuestion(question);
//        }
//
//        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNull(userDAO.selectById(1));
    }
}
