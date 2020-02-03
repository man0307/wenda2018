package com.batman.mq.producer;

import com.batman.async.EventModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author manchaoyang
 * 2018/11/23
 */
@Slf4j
abstract class AbstractEventProducer implements RabbitTemplate.ConfirmCallback {
    /**
     * 重试次数
     */
    private static final int TRY_TIMES = 3;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 先设置本机的最大缓存数量为1000条未收到ack的event
     * 当到达最大数量之后,采用FIFO的淘汰策略,淘汰最老的缓存
     */
    private static int MAX_CACHE_NUMBER = 1000;

    /**
     * 当前缓存的数量
     */
    private AtomicInteger nowCount = new AtomicInteger(0);

    /**
     * 保证缓存的线程安全
     * todo改为使用redis做缓存
     */
    private Map<String, EventModel> eventCacheMap = new ConcurrentHashMap<>(MAX_CACHE_NUMBER);

    /**
     * fifo队列 阻塞队列和ConcurrentHashMap共同实现线程安全的FIFO缓存淘汰
     */
    private ArrayBlockingQueue<String> sequenceFifoQueue = new ArrayBlockingQueue<String>(MAX_CACHE_NUMBER);

    /**
     * 获取实现类的路由键信息
     *
     * @return 路由键
     */
    public abstract String getRoutingKey();

    /**
     * 获取交换机信息
     *
     * @return 交换机信息
     */
    public abstract String getExchangeName();


    /**
     * 要发送事件到队列当中就实现这个接口
     *
     * @param eventModel 事件实体
     */
    public void sendEvent(EventModel eventModel) {
        if (Objects.isNull(eventModel)) {
            log.error("sendEvent error,EventModel is null");
            throw new IllegalArgumentException("eventModel is null");
        }
        RabbitTemplate rabbitTemplate = getRabbitTemplate();
        CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送事件：" + eventModel);
        //发消息
        rabbitTemplate.convertAndSend(getExchangeName(),
                getRoutingKey(),
                eventModel,
                correlation);
        addEventCache(correlation.getId(), eventModel);
    }


    /**
     * 用于重复发送
     *
     * @param correlationId 关联ID
     * @param eventModel    事件实体
     */
    public void resendEvent(String correlationId, EventModel eventModel) {
        if (Objects.isNull(correlationId) || Objects.isNull(eventModel)) {
            log.error("resendEvent error,correlationId is null or eventModel is null correlationId={}", correlationId);
            throw new IllegalArgumentException("resendEvent error,correlationId is null or eventModel is null");

        }
        RabbitTemplate rabbitTemplate = getRabbitTemplate();
        CorrelationData correlation = new CorrelationData(correlationId);
        rabbitTemplate.convertAndSend(getExchangeName(),
                getRoutingKey(),
                eventModel,
                correlation);
    }


    public abstract RabbitTemplate getRabbitTemplate();

    /**
     * 添加缓存
     *
     * @param correlationId 消息ID
     * @param eventModel    消息体
     * @return 是否添加缓存成功
     */
    public boolean addEventCache(String correlationId, EventModel eventModel) {
        //如果队列是满的 那么无论此时其他线程是插入还是删除操作
        // 当前线程执行 操作（删除&插入） 并没有问题 只是有小概率的情况下队列被清空了 阻塞队列取不到数据抛出异常
        if (nowCount.get() == MAX_CACHE_NUMBER) {
            try {
                String key = sequenceFifoQueue.take();
                eventCacheMap.remove(key);
            } catch (InterruptedException e) {
                log.error("sequenceFIFOQueue is empty");
                e.printStackTrace();
                return false;
            }
        } else {
            if (lock.tryLock()) {
                try {
                    if (nowCount.incrementAndGet() <= MAX_CACHE_NUMBER) {
                        sequenceFifoQueue.offer(correlationId);
                        eventCacheMap.put(correlationId, eventModel);
                    } else {
                        nowCount.decrementAndGet();
                        addEventCache(correlationId, eventModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    lock.unlock();
                }

            }
        }
        return true;
    }

    public void deleteEventCache(String correlationId) {
        try {
            eventCacheMap.remove(correlationId);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 返回值可能为空
     *
     * @param correlationId
     * @return
     */
    public EventModel getEventModel(String correlationId) {
        return eventCacheMap.get(correlationId);
    }


    /**
     * MQ的消息确认 如果ack = true 那么就将该消息的缓存删除， 如果为false 那么就重发该消息
     * 如果重发次数大于3次 那么就打上日志 将该消息的缓存删除并且不再重发
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            deleteEventCache(correlationData.getId());
            log.info("消息成功消费");
        } else {
            EventModel event = getEventModel(correlationData.getId());
            if (null != event) {
                if (event.getResendCount() >= TRY_TIMES) {
                    log.error("该消息发送失败:" + event.toString());
                } else {
                    event.setResendCount(event.getResendCount() + 1);
                    resendEvent(correlationData.getId(), event);
                }
            }
            log.info("重新发送消息:" + correlationData.getId());
        }
    }

}
