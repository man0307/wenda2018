package com.batman.service.serviceImpl;

import com.batman.dao.QuestionMapper;
import com.batman.model.Question;
import com.batman.service.QuestionService;
import com.batman.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    SensitiveService sensitiveService;

    @Override
    public int addQuestion(Question question) {
        //敏感词过滤 Html标签过滤 HtmlUtils
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(sensitiveService.filer(question.getTitle()));
        question.setContent(sensitiveService.filer(question.getContent()));
        int res=questionMapper.insert(question);
        return res>0?res:0;
    }

    @Override
    public List<Question> selectByLimit(Integer id, Integer offset, Integer limit) {
        return questionMapper.selectByLimit(id,offset,limit);
    }

    @Override
    public Question selectByQuestionId(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Question record) {
        return questionMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public List<Question> getQuestionsByKeyword(String keyword) {
        return questionMapper.getQuestionsByKeyword(keyword);
    }
}
