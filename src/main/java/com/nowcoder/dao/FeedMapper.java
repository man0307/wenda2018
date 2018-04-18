package com.nowcoder.dao;

import com.nowcoder.model.Feed;
import com.nowcoder.model.FeedExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "feedMapper")
public interface FeedMapper {
    int countByExample(FeedExample example);

    int deleteByExample(FeedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Feed record);

    int insertSelective(Feed record);

    List<Feed> selectByExample(FeedExample example);

    Feed selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Feed record, @Param("example") FeedExample example);

    int updateByExample(@Param("record") Feed record, @Param("example") FeedExample example);

    int updateByPrimaryKeySelective(Feed record);

    int updateByPrimaryKey(Feed record);

    List<Feed> selectUserById(Integer id);
    //拉模式
    List<Feed> selectUserFeeds(@Param("maxId")Integer maxId,@Param("userIds")List<Integer> userIds,@Param("count")Integer count);
}