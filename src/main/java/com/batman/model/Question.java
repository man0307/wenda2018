package com.batman.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yehuo
 */
@Data
public class Question {
    private Integer id;

    private String title;

    private Integer userId;

    private Date createdDate;

    private Integer commentCount;

    private String content;
}