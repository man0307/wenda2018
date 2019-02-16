package com.batman.dao;

import java.util.List;
import com.batman.model.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value ="questionMapper")
public interface QuestionMapper {
    int countByExample(QuestionExample example);

    int deleteByExample(QuestionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    List<Question> selectByExampleWithBLOBs(QuestionExample example);

    List<Question> selectByLimit(@Param("userId") Integer userId, @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);


    List<Question> selectByExample(QuestionExample example);

    Question selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByExampleWithBLOBs(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByExample(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Message> selectMessageByLimit(@Param("conversationId")String conversationId,@Param("offset")Integer offset,@Param("limit")Integer limit);

    List<Question> getQuestionsByKeyword(@Param("keyword")String keyword);

}