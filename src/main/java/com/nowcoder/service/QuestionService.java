package com.nowcoder.service;

import com.nowcoder.model.Question;

import java.util.List;

public interface QuestionService {
    int addQuestion(Question question);

    List<Question> selectByLimit(Integer id, Integer offset, Integer limit);

    Question selectByQuestionId(Integer id);

    int updateByPrimaryKeyWithBLOBs(Question record);

    //模糊查询
    List<Question> getQuestionsByKeyword(String keyword);
}
