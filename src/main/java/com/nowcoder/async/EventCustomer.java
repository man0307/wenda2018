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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventCustomer implements InitializingBean,ApplicationContextAware {
    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType,List<EventHandler>> config=new HashMap<>();

    private ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        //spring利用反射机制可以轻松获得所有EventHandler来实现注册
          Map<String,EventHandler> handlerMap=applicationContext.getBeansOfType(EventHandler.class);
          for(Map.Entry<String,EventHandler> eventHandlerEntry:handlerMap.entrySet()){
              List<EventType> eventTypes=eventHandlerEntry.getValue().getEventHandlerSupport();
              for(EventType type:eventTypes){
                  if(!config.containsKey(type)){
                      config.put(type,new ArrayList<EventHandler>());
                  }
                  config.get(type).add(eventHandlerEntry.getValue());
              }
          }

          //消费者队列的实现
          Thread customer=new Thread(new Runnable() {
              @Override
              public void run() {
                  while (true){
                      String key=JedisKeyUtil.getEventQueueKey();
                      List<String> events=jedisAdapter.brpop(0, key);
                      for(String event:events){
                          if(event.equals(key)){
                              continue;
                          }


                          //JSON的反序列化
                          EventModel eventModel= JSON.parseObject(event,EventModel.class);
                          if(!config.containsKey(eventModel.getType())){
                              try {
                                  throw new Exception("不可以识别的事件类型");
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                          for(EventHandler handler:config.get(eventModel.getType())){
                              handler.doHandler(eventModel);
                          }
                      }
                  }
              }
          });
        customer.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext=applicationContext;
    }
}
