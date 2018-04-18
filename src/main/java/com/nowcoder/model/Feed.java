package com.nowcoder.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private Integer id;

    private Date createdDate;

    private Integer userId;

    //JSON存储各种字段
    private String data;

    private Integer type;

    private JSONObject jsonObject=null;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
        jsonObject=JSONObject.parseObject(data);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String get(String key){
        return jsonObject==null?null:jsonObject.getString(key);
    }
}