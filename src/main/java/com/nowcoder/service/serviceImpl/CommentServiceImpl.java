package com.nowcoder.service.serviceImpl;

import com.nowcoder.dao.CommentMapper;
import com.nowcoder.model.Comment;
import com.nowcoder.model.CommentExample;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SensitiveService sensitiveService;

    @Override
    public int insert(Comment record) {
        //敏感词过滤
        record.setContent(HtmlUtils.htmlEscape(record.getContent()));
        record.setContent(sensitiveService.filer(record.getContent()));
        return commentMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Comment selectByPrimaryKey(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Comment> selectByEntity(Integer entityId, Integer entutyType) {
        return commentMapper.selectByEntity(entityId,entutyType);
    }

    @Override
    public int updateByPrimaryKeySelective(Comment record) {
        return commentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Comment record) {
        return commentMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int countByExample(CommentExample example) {
        return commentMapper.countByExample(example);
    }
}
