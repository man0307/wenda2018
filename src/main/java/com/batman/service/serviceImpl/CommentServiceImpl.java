package com.batman.service.serviceImpl;

import com.batman.dao.CommentMapper;
import com.batman.model.Comment;
import com.batman.model.CommentExample;
import com.batman.service.CommentService;
import com.batman.service.SensitiveService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static Logger logger = Logger.getLogger(CommentService.class);


    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SensitiveService sensitiveService;

    @Override
    public int insert(Comment record) {
        //敏感词过滤
        record.setContent(HtmlUtils.htmlEscape(record.getContent()));
        record.setContent(sensitiveService.filer(record.getContent()));
        int cord = 0;
        try {
            cord = commentMapper.insert(record);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        int cord = 0;
        try {
            cord = commentMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public Comment selectByPrimaryKey(Integer id) {
        Comment cord = null;
        try {
            cord = commentMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public List<Comment> selectByEntity(Integer entityId, Integer entutyType) {
        List<Comment>  cords = null;
        try {
            cords = commentMapper.selectByEntity(entityId,entutyType);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }

    @Override
    public int updateByPrimaryKeySelective(Comment record) {
        int  cords = 0;
        try {
            cords = commentMapper.updateByPrimaryKeySelective(record);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Comment record) {
        int  cords = 0;
        try {
            cords = commentMapper.updateByPrimaryKeyWithBLOBs(record);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }

    @Override
    public int countByExample(CommentExample example) {
        int  cords = 0;
        try {
            cords = commentMapper.countByExample(example);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;

    }
}
