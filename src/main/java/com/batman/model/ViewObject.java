package com.batman.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    Map<String,Object> vo=new HashMap<>();
    public Object get(String key){
        return vo.get(key);
    }
    public void set(String key,Object o){
      vo.put(key,o);
    }
}
