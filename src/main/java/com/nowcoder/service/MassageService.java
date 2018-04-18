package com.nowcoder.service;

import com.nowcoder.model.Message;
import com.nowcoder.model.MessageExample;

import java.util.List;

public interface MassageService {
    List<Message> getMessageByLimit(String conversationId, Integer offset, Integer limit);

    int countByExample(MessageExample example);

    int deleteByExample(MessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Message record);


    List<Message> selectByExampleWithBLOBs(MessageExample example);

    List<Message> selectByExample(MessageExample example);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);


    int updateByPrimaryKey(Message record);

    List<Message> getConversationList(Integer userId,Integer offset,Integer limit);

    Integer getConversationUnreadCount(Integer userId,String conversationId);
}
