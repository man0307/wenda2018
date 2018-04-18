package com.nowcoder.service.serviceImpl;

import com.nowcoder.service.FollowService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    JedisAdapter jedisAdapter;


    @Override
    public boolean follow(Integer userId, Integer entityId, Integer entityType) {
        String followerKey= JedisKeyUtil.getFollowerKey(entityId,entityType);
        String followeeKey=JedisKeyUtil.getFolloweeKey(userId,entityType);
        Jedis jedis=jedisAdapter.getJedis();
        //开启事务
        Transaction transaction=jedisAdapter.multi(jedis);
        Date date=new Date();
        //被关注对象的粉丝数+1
        transaction.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //关注者所关注的实体+1
        transaction.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> list=jedisAdapter.exec(transaction,jedis);
        System.out.println(jedisAdapter.zcard(followerKey));
        return list.size()==2&&(Long)list.get(0)>=0&&(Long)list.get(1)>=0;
    }

    @Override
    public boolean unFollow(Integer userId, Integer entityId, Integer entityType) {
        String followerKey= JedisKeyUtil.getFollowerKey( entityId,  entityType);
        String followeeKey=JedisKeyUtil.getFolloweeKey( userId,  entityType);

        Jedis jedis=jedisAdapter.getJedis();
        //开启事务
        Transaction transaction=jedisAdapter.multi(jedis);
        Date date=new Date();
        transaction.zrem(followerKey,String.valueOf(userId));
        transaction.zrem(followeeKey,String.valueOf(entityId));
        List<Object> list=jedisAdapter.exec(transaction,jedis);
        return list.size()==2&&(Long)list.get(0)>=0&&(Long)list.get(1)>=0;
    }

    @Override
    public List<Integer> getFollowees(Integer userId, Integer entityType, Integer count) {
        String followeeKey=JedisKeyUtil.getFolloweeKey( userId,  entityType);
        return getIds(jedisAdapter.zrevrange(followeeKey,0,count));
    }

    @Override
    public List<Integer> getFollowers(Integer entityId, Integer entityType,Integer count) {
        String followerKey= JedisKeyUtil.getFollowerKey( entityId,  entityType);
        return getIds(jedisAdapter.zrevrange(followerKey,0,count));
    }

    @Override
    public List<Integer> getFollowees(Integer userId, Integer entityType, Integer offsert, Integer count) {
        String followeeKey=JedisKeyUtil.getFolloweeKey( userId,  entityType);
        return getIds(jedisAdapter.zrevrange(followeeKey,offsert,offsert+count));
    }

    @Override
    public List<Integer> getFollowers(Integer entityId, Integer entityType, Integer offsert, Integer count) {
        String followerKey= JedisKeyUtil.getFollowerKey( entityId,  entityType);
        return getIds(jedisAdapter.zrevrange(followerKey,offsert,offsert+count));
    }

    @Override
    public Long getFollowerCount(Integer entityId, Integer entityType) {
        String followerKey= JedisKeyUtil.getFollowerKey( entityId,  entityType);
        return jedisAdapter.zcard(followerKey);
    }

    @Override
    public Long getFolloweeCount(Integer userId, Integer entityType) {
        String followeeKey=JedisKeyUtil.getFolloweeKey( userId,  entityType);
        return jedisAdapter.zcard(followeeKey);
    }
    @Override
    public Boolean isFollower(Integer userId, Integer entityId, Integer entityType) {
        String followerKey= JedisKeyUtil.getFollowerKey( entityId,  entityType);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=null;
    }
    private List<Integer> getIds(Set<String> set){
        List<Integer> ids=new ArrayList<>();
        for(String id:set){
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
