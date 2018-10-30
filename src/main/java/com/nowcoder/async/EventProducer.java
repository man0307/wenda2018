package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private static Logger logger = Logger.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    //redis异步队列的入队操作
    public Boolean fireEvent(EventModel eventModel){
        try {
            String value= JSONObject.toJSONString(eventModel);
            jedisAdapter.lpush(JedisKeyUtil.getEventQueueKey(),value);
            return true;
        }catch (Exception e){
             logger.error(e.getMessage());
             return false;
        }
    }
}
