package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.JedisKeyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Overwrite by manchaoyang on 2018/10/21.
 */
@Service
public class EventCustomer implements InitializingBean, ApplicationContextAware {
    @Autowired
    private JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //spring利用反射机制可以轻松获得所有EventHandler来实现注册
        Map<String, EventHandler> handlerMap = applicationContext.getBeansOfType(EventHandler.class);
        for (Map.Entry<String, EventHandler> eventHandlerEntry : handlerMap.entrySet()) {
            List<EventType> eventTypes = eventHandlerEntry.getValue().getEventHandlerSupport();
            for (EventType type : eventTypes) {
                if (!config.containsKey(type)) {
                    config.put(type, new ArrayList<EventHandler>());
                }
                config.get(type).add(eventHandlerEntry.getValue());
            }
        }

        //消费者队列的实现
        Thread customer = new Thread(new Runnable() {
            @Override
            public void run() {
                String key = JedisKeyUtil.getEventQueueKey();
                while (true) {
                    try {
                    //timeout设置为0 则此redis的list变为了阻塞队列
                    List<String> events = jedisAdapter.brpop(0, key);
                    if (CollectionUtils.isEmpty(events) || events.size() < 2) {
                        //todo logger
                        continue;
                    }
                    EventModel eventModel = JSON.parseObject(events.get(1), EventModel.class);
                    EventType eventType = eventModel.getType();
                    if (!config.containsKey(eventType)) {
                        try {
                            //todo logger
                            throw new Exception("不可以识别的事件类型");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (EventHandler handler : config.get(eventType)) {
                        handler.doHandler(eventModel);
                    }
                  }catch (Exception e){
                    //todo logger
                  }
                }
            }
        });
        customer.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
