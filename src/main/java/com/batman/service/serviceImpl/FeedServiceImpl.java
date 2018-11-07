package com.batman.service.serviceImpl;

import com.batman.dao.FeedMapper;
import com.batman.model.Feed;
import com.batman.model.FeedExample;
import com.batman.service.CommentService;
import com.batman.service.FeedService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl  implements FeedService{

    private static Logger logger = Logger.getLogger(FeedService.class);


    @Autowired
    FeedMapper feedMapper;

    @Override
    public int countByExample(FeedExample example) {
        int cord = 0;
        try{
            cord = feedMapper.countByExample(example);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public int deleteByExample(FeedExample example) {
        int cord = 0;
        try{
            cord = feedMapper.deleteByExample(example);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        int cord = 0;
        try{
            cord = feedMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public int addFeed(Feed record) {
        int cord = 0;
        try{
            cord = feedMapper.insert(record);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public List<Feed> selectByExample(FeedExample example) {
        List<Feed> cords = null;
        try{
            cords = feedMapper.selectByExample(example);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }

    @Override
    public Feed selectByPrimaryKey(Integer id) {
        Feed  cord = null;
        try{
            cord  = feedMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public int updateByPrimaryKey(Feed record) {
        int cord = 0;
        try{
            cord = feedMapper.updateByPrimaryKey(record);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cord;
    }

    @Override
    public List<Feed> selectUserById(Integer id) {
        List<Feed> cords = null;
        try{
            cords = feedMapper.selectUserById(id);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }

    @Override
    public List<Feed> selectUserFeeds(Integer maxId, List<Integer> userIds, Integer count) {
        List<Feed> cords = null;
        try{
            cords = feedMapper.selectUserFeeds(maxId,userIds,count);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return cords;
    }
}
