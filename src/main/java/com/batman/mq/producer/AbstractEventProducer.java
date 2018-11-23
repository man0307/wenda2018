package com.batman.mq.producer;

import com.batman.async.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author manchaoyang
 * 2018/11/23
 */
abstract class AbstractEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionEvenProducer.class);

    private ReentrantLock lock = new ReentrantLock();
    /**
     * 先设置本机的最大缓存数量为10000条未收到ack的event
     * 当到达最大数量之后,采用FIFO的淘汰策略,淘汰最老的缓存
     */
    private static int MAX_CACHE_NUMBER = 1000;

    /**
     * 当前缓存的数量
     */
    private AtomicInteger nowCount = new AtomicInteger(0);


    /**
     * 保证缓存的线程安全
     */
    private Map<String, EventModel> eventCacheMap = new ConcurrentHashMap<>(MAX_CACHE_NUMBER);

    /**
     * fifo队列 阻塞队列和ConcurrentHashMap共同实现线程安全的FIFO缓存淘汰
     */
    private ArrayBlockingQueue<String> sequenceFIFOQueue = new ArrayBlockingQueue<String>(MAX_CACHE_NUMBER);

    /**
     * 要发送事件到队列当中就实现这个接口
     *
     * @param eventModel
     */
    public abstract void sendEvent(EventModel eventModel);

    /**
     * 用于重复发送
     *
     * @param correlationId
     * @param eventModel
     */
    public abstract void sendEvent(String correlationId, EventModel eventModel);

    /**
     * 添加缓存
     *
     * @param correlationId
     * @param eventModel
     * @return
     */
    public boolean addEventCache(String correlationId, EventModel eventModel) {
        //如果队列是满的 那么无论此时其他线程是插入还是删除操作
        // 当前线程执行 操作（删除&插入） 并没有问题 只是有小概率的情况下队列被清空了 阻塞队列取不到数据抛出异常
        if (nowCount.get() == MAX_CACHE_NUMBER) {
            try {
                System.out.println("满了要删除");
                String key = sequenceFIFOQueue.take();
                eventCacheMap.remove(key);
                System.out.println("满了删除");
            } catch (InterruptedException e) {
                logger.error("sequenceFIFOQueue is empty");
                e.printStackTrace();
                return false;
            }
        } else {
            if (lock.tryLock()) {
                try {
                    if (nowCount.incrementAndGet() <= MAX_CACHE_NUMBER) {
                        sequenceFIFOQueue.offer(correlationId);
                        eventCacheMap.put(correlationId, eventModel);
                    } else {
                        nowCount.decrementAndGet();
                        System.out.println(nowCount.get() + "重新插入");
                        addEventCache(correlationId, eventModel);
                    }
                    System.out.println(Thread.currentThread().getName() + "线程 插入第:" + nowCount.get() + " 个");
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
            logger.error(e.getMessage());
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


}
