package com.nowcoder.service.serviceImpl;

import com.nowcoder.dao.MessageMapper;
import com.nowcoder.model.Message;
import com.nowcoder.model.MessageExample;
import com.nowcoder.service.MassageService;
import com.nowcoder.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageServiceImpl implements MassageService {

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Message> getMessageByLimit(String conversationId, Integer offset, Integer limit) {
        return messageMapper.selectMessageByLimit(conversationId,offset,limit);
    }

    @Override
    public int countByExample(MessageExample example) {
        return messageMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(MessageExample example) {
        return messageMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Message record) {
        record.setContent(HtmlUtils.htmlEscape(record.getContent()));
        record.setContent(sensitiveService.filer(record.getContent()));
        return messageMapper.insert(record);
    }

    @Override
    public List<Message> selectByExampleWithBLOBs(MessageExample example) {
        return messageMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Message> selectByExample(MessageExample example) {
        return messageMapper.selectByExample(example);
    }

    @Override
    public Message selectByPrimaryKey(Integer id) {
        return messageMapper.selectByPrimaryKey(id);
    }




    @Override
    public int updateByPrimaryKeySelective(Message record) {
        return messageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Message record) {
        return messageMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Message record) {
        return messageMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Message> getConversationList(Integer userId, Integer offset, Integer limit) {
        return messageMapper.getConversationList(userId,offset,limit);
    }

    @Override
    public Integer getConversationUnreadCount(Integer userId, String conversationId) {
        return messageMapper.getConversationUnreadCount(userId,conversationId);
    }
}
