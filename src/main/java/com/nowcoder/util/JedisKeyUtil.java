package com.nowcoder.util;

public class JedisKeyUtil {
    private static String SPLIT=":";
    private static String LIKE="LIKE";
    private static String DISLIKE="DISLIKE";
    private static String BIZ_EVENTQUEUE="EVENTQUEUE";
    private static String FOLLOWER="FOLLOWER";
    private static String FOLLOWEE="FOLLOWEE";
    private static String TIMELINE="TIMELINE";


    public static String getLikeKeyByEntityIdAndEntityType(Integer entityId ,Integer entityType){
          return LIKE+SPLIT+entityId+"_"+String.valueOf(entityType)+SPLIT+String.valueOf(entityType);
    }

    public static String getDISLIKEeKeyByEntityIdAndEntityType(Integer entityId ,Integer entityType){
        return DISLIKE+SPLIT+entityId+"_"+String.valueOf(entityType)+SPLIT+String.valueOf(entityType);
    }
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    public static String getFollowerKey(Integer entityId ,Integer entityType){
         return  FOLLOWER+SPLIT+String.valueOf(entityId)+"_"+String.valueOf(entityType);
    }

    //每个用户对某类实体的关注
    public static  String getFolloweeKey(Integer userId, Integer entityType){
        return  FOLLOWEE+SPLIT+String.valueOf(userId)+"_"+String.valueOf(entityType);
    }

    public static String getTimelineKey(Integer uid){
        return TIMELINE+SPLIT+uid;
    }
}
