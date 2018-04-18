package com.nowcoder.service.serviceImpl;

import com.nowcoder.dao.FeedMapper;
import com.nowcoder.model.Feed;
import com.nowcoder.model.FeedExample;
import com.nowcoder.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl  implements FeedService{
    @Autowired
    FeedMapper feedMapper;

    @Override
    public int countByExample(FeedExample example) {
        return feedMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(FeedExample example) {
        return feedMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return feedMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addFeed(Feed record) {
        return feedMapper.insert(record);
    }

    @Override
    public List<Feed> selectByExample(FeedExample example) {
        return feedMapper.selectByExample(example);
    }

    @Override
    public Feed selectByPrimaryKey(Integer id) {
        return feedMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Feed record) {
        return feedMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Feed> selectUserById(Integer id) {
        return feedMapper.selectUserById(id);
    }

    @Override
    public List<Feed> selectUserFeeds(Integer maxId, List<Integer> userIds, Integer count) {
        return feedMapper.selectUserFeeds(maxId,userIds,count);
    }
}
