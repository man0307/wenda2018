package com.batman.async;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.batman.util.JedisAdapter;
import com.batman.util.JedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * Overwrite by manchaoyang on 2018/10/21.
 */
@Service
public class EventCustomer implements InitializingBean, ApplicationContextAware {

    private static Logger logger = Logger.getLogger(EventCustomer.class);

    private static ThreadFactory eventCustomerThreadFactory = new ThreadFactoryBuilder().setNameFormat("event-customer-pool-%d").build();
    private static ExecutorService customerExecutorService = new ThreadPoolExecutor(4, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10), eventCustomerThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    /**
     * 实现ApplicationContextAware接口的目的:ApplicationContextAware是BeanFactory的子类在Spring容器完成初始化之后就可以获取容器内的对象
     * 我们通过ApplicationContextAware来获取EventHandler类型 这几个相关的接口都跟Spring的生命周期有关系
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //spring利用反射机制可以轻松获得所有EventHandler来实现注册
        Map<String, EventHandler> handlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (CollectionUtils.isEmpty(handlerMap)) {
            logger.info("no event handler registered.");
        }
        for (Map.Entry<String, EventHandler> eventHandlerEntry : handlerMap.entrySet()) {
            List<EventType> eventTypes = eventHandlerEntry.getValue().getEventHandlerSupport();
            for (EventType type : eventTypes) {
                if (!config.containsKey(type)) {
                    config.put(type, new ArrayList<EventHandler>());
                }
                config.get(type).add(eventHandlerEntry.getValue());
            }
        }
        String key = JedisKeyUtil.getEventQueueKey();
        customerExecutorService.execute(()->{
            int i = 0;
            while (true) {
                try {
                    //timeout设置为0 则此redis的list变为了阻塞队列
                    List<String> event = jedisAdapter.brpop(0, key);
                    if (CollectionUtils.isEmpty(event) || event.size() < 2) {
                        continue;
                    }
                    customerExecutorService.execute(new eventCustomerThread(event));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    logger.error(e.getMessage());
                }
            }
        });

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class eventCustomerThread implements Runnable {

        private List<String> event;

        /**
         * 上游提供event可用性保證
         *
         * @param event
         */
        public eventCustomerThread(List<String> event) {
            this.event = event;
        }

        @Override
        public void run() {
            String key = JedisKeyUtil.getEventQueueKey();
            try {
                EventModel eventModel = JSON.parseObject(event.get(1), EventModel.class);
                EventType eventType = eventModel.getType();
                if (!config.containsKey(eventType)) {
                    try {
                        logger.error("event type is not recognized. ");
                        throw new Exception("不可以识别的事件类型");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (EventHandler handler : config.get(eventType)) {
                    handler.doHandler(eventModel);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
