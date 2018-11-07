package com.batman.async;

public enum EventType {
    /**
     * 喜欢/点赞 ps: xxx赞了你
     */
    LIKE(0),

    /**
     * 评论 ps：xxx评论了你
     */
    COMMENT(1),

    /**
     * 登录
     */
    LOGIN(2),

    /**
     * 邮件
     */
    MAIL(3),

    /**
     * 关注
     */
    FOLLOW(4),

    /**
     * 取消关注
     */
    UNFOLLOW(5),

    /**
     * 提问
     */
    ADD_QUESTION(6);

    private Integer type;
    EventType(Integer type){
        this.type=type;
    }

    public Integer getValue(){
        return type;
    }

    public EventType typeFrom(Integer typeValue){
        for(EventType type : EventType.values()){
            if(type.type.equals(typeValue)){
                return type;
            }
        }
        return null;
    }
}
