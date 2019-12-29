package com.batman.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @author yehuo
 */
@Data
public class Feed {

    private Integer id;

    /**
     * 创建日期
     */
    private Date createdDate;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * JSON格式的数据
     */
    private String data;
    /**
     * 类型
     */
    private Integer type;

    private JSONObject jsonObject = null;

    public String get(String key) {
        return jsonObject == null ? null : jsonObject.getString(key);
    }
}