package com.nowcoder.async;

public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    ADD_QUESTION(6);
    private Integer type;
    EventType(Integer type){
        this.type=type;
    }
    public Integer getValue(){
        return type;
    }
}
