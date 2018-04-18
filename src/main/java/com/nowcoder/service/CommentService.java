package com.nowcoder.service;

import com.nowcoder.model.Comment;
import com.nowcoder.model.CommentExample;

import java.util.List;

public interface CommentService {
    int insert(Comment record);

    int deleteByPrimaryKey(Integer id);

    Comment selectByPrimaryKey(Integer id);

    List<Comment> selectByEntity(Integer entityId, Integer entutyType);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int countByExample(CommentExample example);
}
