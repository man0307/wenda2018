package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventModel;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import javax.lang.model.element.ExecutableElement;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

//
@Service
public class JedisAdapter implements InitializingBean{

    private  JedisPool pool;
    //初始化redis连接池
    public JedisAdapter(){
        pool=new JedisPool("redis://localhost:6379/9");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
           pool=new JedisPool("redis://localhost:6379/9");
    }
    //点赞点踩的功能用set即可
    public void sadd(String key,String value){
       Jedis jedis=null;
         try{
             jedis=pool.getResource();
            jedis.sadd(key,value);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
   }
    public Long srem(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0l;
    }
    public Long scard(String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0l;
    }
    public Boolean sismember(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;
    }

    //redis实现异步队列的接口

    public void lpush(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            jedis.lpush(key,value);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> lrange(String key,Integer beg,Integer end){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.lrange(key,beg,end);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    //redis的brpop作用：如果队列中没有元素就等待timeout时间长度然后
    // 返回一个假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。
    // 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
    public List<String> brpop(Integer timeout, String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
           return jedis.brpop(timeout,key);
        }catch (Exception e){
            //logger
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    //redis事务
//    public Jedis getJedis(){
//        try {
//            return pool.getResource();
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//    //开启事务
//    public Transaction multi(Jedis jedis){
//          try {
//              return jedis.multi();
//          }catch (Exception e){
//              return null;
//          }
//    }
//
//    //提交事务
//    public List<Object> exec(Transaction transaction,Jedis jedis){
//        try{
//             return transaction.exec();
//        }catch (Exception e){
//            transaction.discard();
//        }finally {
//            try{
//                transaction.close();
//            } catch (IOException e) {
//                //logger
//            }
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return null;
//    }
//
//    public Long zadd(String key,double score,String value){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zadd(key,score,value);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return 0l;
//    }
//
//
//    public Long zadd(String key,double score,String value,Jedis jedis){
//        try{
//            return jedis.zadd(key,score,value);
//        }catch (Exception e){
//            //logger
//        }
//        return 0l;
//    }
//
//
//    public Long zrem(String key,String value){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zrem(key,value);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return 0l;
//    }
//
//    public Long zcard(String key){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zcard(key);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return 0l;
//    }
//
//    public Set<String> zrange(String key,Integer beg,Integer end){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zrange(key,beg,end);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return null;
//    }
//
//    public Set<String> zrevrange(String key,Integer beg,Integer end){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zrevrange(key,beg,end);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return null;
//    }
//
//    public Double zscore(String key,String value){
//        Jedis jedis=null;
//        try{
//            jedis=pool.getResource();
//            return jedis.zscore(key,value);
//        }catch (Exception e){
//            //logger
//        }finally {
//            if(jedis!=null){
//                jedis.close();
//            }
//        }
//        return null;
//    }

    // /练习的代码
    public static void print(int index,Object obj){
        System.out.println(String.format("%d %s",index,obj.toString()));
    }
    public static void main(String[] agrs) throws InterruptedException {
        Jedis jedis=new Jedis("redis://localhost:6379/9");
//        EVENTQUEUE
        for(int i=0;i<9;i++){
        System.out.println(jedis.lpop("TIMELINE:41"));}
////        jedis.set("name","wang");
//        print(1,jedis.get("name"));
//        //设置延迟数据库自动删除 一般用于验证码之类的
//        jedis.setex("yanzhen",1,"测试自动删除");
//        print(2,jedis.get("yanzhen"));
////        Thread.sleep(2000);
////        print(2,jedis.get("yanzhen"));
//        String listName="list";
//        for(int i=0;i<10;i++){
//            jedis.lpush(listName,"a"+i);
//        }
//        print(3,jedis.lrange(listName,0,4));
//
//        //hash
//        String userKey="userxxx";
//        jedis.hset(userKey,"name","jim");
//        jedis.hset(userKey,"adddress","nj");
//        jedis.hset(userKey,"age","22");
//        print(4,jedis.hget(userKey,"name"));
//        jedis.hdel(userKey,"name");
//        print(4,jedis.hget(userKey,"age"));
//        print(4,jedis.hexists(userKey,"age"));
//        print(4,jedis.hexists(userKey,"name"));
//
//        //set
//        String set1="set1";
//        String set2="set2";
//        for(int i=0;i<10;i++){
//            jedis.sadd(set1,i+"");
//            jedis.sadd(set2,i*i+"");
//        }
//        print(12,jedis.smembers(set1));
//        print(13,jedis.smembers(set2));
//        print(14,jedis.sunion(set2,set1));
//        print(15,jedis.sdiff(set2,set1));
//
//        jedis.smove(set2,set1,"25");
//
//        print(16,jedis.smembers(set1));
//        print(17,jedis.smembers(set2));
//
//        System.out.println(jedis.srandmember(set1));
//
//        //优先队列  SortedSet 根据权重排序
//        String rankKey="rankKey";
//        jedis.zadd(rankKey,12,"jim");
//        jedis.zadd(rankKey,132,"jim1");
//        jedis.zadd(rankKey,1,"jim2");
//        jedis.zadd(rankKey,2,"jim3");
//        jedis.zadd(rankKey,2212,"jim4");
//        jedis.zadd(rankKey,12323,"jim5");
//        jedis.zadd(rankKey,121,"jim6");
//        print(18,jedis.zcard(rankKey));
//        print(19,jedis.zcount(rankKey,0,100));
//
//        print(20,jedis.zscore(rankKey,"jim"));
//        //默认从小到大排序
//        print(21,jedis.zrange(rankKey,0,100));
//        print(22,jedis.zrevrange(rankKey,0,100));
//
//        //打印出来1到1000分的人
//        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"0","1000")){
//            System.out.println(tuple.getElement()+": "+tuple.getScore());
//        }
//        print(23,jedis.zrank(rankKey,"jim"));
//        print(24,jedis.zrevrank(rankKey,"jim"));
//        String setKey="zset";
//        //默认是八条连接
//        JedisPool pool=new JedisPool();
//        for(int i=0;i<100;i++){
//            Jedis j=pool.getResource();
//            j.close();
//        }
//        User user=new User();
//        user.setName("mcy");
//        user.setPassword("sdfsdf");
//        user.setSalt("asdf");
//        user.setHeadUrl("sfsd");
//        user.setId(123123);
//
//
//        //JSON的序列化
//        String value= JSONObject.toJSONString(user);
//        System.out.println(value);
//        User user2= JSON.parseObject(value,User.class);
//        System.out.println(user2.getName());


    }
    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                    // ..
                }
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
