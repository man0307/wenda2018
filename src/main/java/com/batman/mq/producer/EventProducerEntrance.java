package com.batman.mq.producer;

import com.batman.annotation.EvenTypeAnnotation;
import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author manchaoyang
 * 2018/11/24
 * 外部统一使用该事件入口类对事件进行注入
 */
@Component("eventProducerEntrance")
public class EventProducerEntrance implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventProducerEntrance.class);

    /**
     * 用线程池来做事件分发
     */
    private static ThreadFactory eventCustomerThreadFactory = new ThreadFactoryBuilder().setNameFormat("event-customer-pool-%d").build();
    private static ExecutorService customerExecutorService = new ThreadPoolExecutor(4, 30,
            10L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(100), eventCustomerThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    private ApplicationContext applicationContext;

    /**
     * 因为多线程条件下是只读的操作 所以事件关联表只选用HashMap
     */
    private Map<EventType, List<AbstractEventProducer>> eventTypeMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 将所有AbstractEventProducer的子类取出 提取出注解标记并注册进入相应的事件类型对应列表当中
         */
        Map<String, AbstractEventProducer> eventProducerMap = applicationContext.getBeansOfType(AbstractEventProducer.class);
        for (Map.Entry<String, AbstractEventProducer> entry : eventProducerMap.entrySet()) {
            AbstractEventProducer eventProducer = entry.getValue();
            EvenTypeAnnotation annotation = eventProducer.getClass().getAnnotation(EvenTypeAnnotation.class);
            if (annotation != null) {
                EventType[] eventTypes = annotation.values();
                for (EventType eventType : eventTypes) {
                    List<AbstractEventProducer> list = eventTypeMap.getOrDefault(eventType, new ArrayList<>());
                    list.add(eventProducer);
                    eventTypeMap.put(eventType, list);
                }
            } else {
                logger.info(eventProducer.getClass().getName() + "缺少类型注解信息");
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void fireEvent(EventModel eventModel) {
        List<AbstractEventProducer> list = eventTypeMap.get(eventModel.getType());
        if(!CollectionUtils.isEmpty(list)){
            for(AbstractEventProducer producer : list){
                customerExecutorService.submit(()->{
                    producer.sendEvent(eventModel);
                });
            }
        }
    }
}
