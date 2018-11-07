package com.batman.dao;

import com.batman.model.LoginTicket;
import com.batman.model.LoginTicketExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "loginTicketMapper")
public interface LoginTicketMapper {
    int countByExample(LoginTicketExample example);

    int deleteByExample(LoginTicketExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginTicket record);

    int insertSelective(LoginTicket record);

    List<LoginTicket> selectByExample(LoginTicketExample example);

    LoginTicket selectByPrimaryKey(Integer id);


    LoginTicket selectByTicket(String ticket);


    int updateByExampleSelective(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    int updateByExample(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    int updateByPrimaryKeySelective(LoginTicket record);

    int updateByPrimaryKey(LoginTicket record);
}