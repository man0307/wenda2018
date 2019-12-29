package com.batman.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yehuo
 */
@Data
public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;

    private Date expired;

    private Integer status;
}