package com.batman.util;

import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by batman on 2016/7/3.
 */
public class WendaUtil {
    public static Integer getSystemUserid() {
        return SYSTEM_USERID;
    }

    public static void setSystemUserid(Integer systemUserid) {
        SYSTEM_USERID = systemUserid;
    }

    public static String getSystemUsername() {
        return SYSTEM_USERNAME;
    }

    public static void setSystemUsername(String systemUsername) {
        SYSTEM_USERNAME = systemUsername;
    }

    private static Integer SYSTEM_USERID=13;
    private static String SYSTEM_USERNAME="admin";

    public static String getJSONString(Integer code,String msg){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(Integer code){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toJSONString();
    }
    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {

            return null;
        }
    }
}
