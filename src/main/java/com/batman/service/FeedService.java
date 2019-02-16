package com.batman.service;

import com.batman.model.Feed;
import com.batman.model.FeedExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeedService {
    int countByExample(FeedExample example);

    int deleteByExample(FeedExample example);

    int deleteByPrimaryKey(Integer id);

    int addFeed(Feed record);

    List<Feed> selectByExample(FeedExample example);

    Feed selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Feed record);

    List<Feed> selectUserById(Integer id);
    //拉模式
    List<Feed> selectUserFeeds(@Param("maxId")Integer maxId,@Param("userIds")List<Integer> userIds,@Param("count")Integer count);
}
