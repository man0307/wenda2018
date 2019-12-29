package com.batman.model;

import lombok.Data;

/**
 * @author yehuo
 */
@Data
public class User {
    private Integer id;

    private String name;

    private String password;

    private String salt;

    private String headUrl;
}