package com.batman.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yehuo
 * <p>
 * 评论中心的实体类
 */
@Data
public class Comment {
    /**
     * id
     */
    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 创建日期
     */
    private Date createdDate;
    /**
     * 实体ID
     */
    private Integer entityId;
    /**
     * 实体类型
     */
    private Integer entityType;
    /**
     * 评论内容
     */
    private String content;
}