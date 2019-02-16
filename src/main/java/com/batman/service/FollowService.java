package com.batman.service;

import java.util.List;

public interface FollowService {
    boolean follow(Integer userId,Integer entityId,Integer entityType);

    boolean unFollow(Integer userId,Integer entityId,Integer entityType);

    //获得关注对象
    List<Integer> getFollowees(Integer userId, Integer entityType, Integer count);

    //获取粉丝
    List<Integer> getFollowers(Integer entityId,Integer entityType,Integer count);

    List<Integer> getFollowees(Integer userId, Integer entityType, Integer offsert,Integer count);

    //获取粉丝
    List<Integer> getFollowers(Integer entityId,Integer entityType, Integer offsert,Integer count);

    //获取关注(粉丝)数量
    Long getFollowerCount(Integer entityId,Integer entityType);

    //用户关注该类型实体的数量
    Long getFolloweeCount(Integer userId,Integer entityType);


    //判断是否是其关注者
    Boolean isFollower(Integer userId,Integer entityId,Integer entityType);


}
