package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService{
    @Autowired
    JedisAdapter jedisAdapter;

    public Integer getLikeStatus(Integer userId,Integer commentEntityId,Integer commentEntityType) {
        String likeKey= JedisKeyUtil.getLikeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        String disLikeKey= JedisKeyUtil.getDISLIKEeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)) )return 1;
        if(jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) )return -1;
        return 0;
    }

    public Long like(Integer userId,Integer commentEntityId,Integer commentEntityType){
        String likeKey= JedisKeyUtil.getLikeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        String disLikeKey= JedisKeyUtil.getDISLIKEeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }


    public Long dislike(Integer userId,Integer commentEntityId,Integer commentEntityType){
        String likeKey= JedisKeyUtil.getLikeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        String dislikeKey= JedisKeyUtil.getDISLIKEeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public Long getLikeCount(Integer commentEntityId,Integer commentEntityType){
        String likeKey= JedisKeyUtil.getLikeKeyByEntityIdAndEntityType(commentEntityId,commentEntityType);
        return jedisAdapter.scard(likeKey);
    }
}
