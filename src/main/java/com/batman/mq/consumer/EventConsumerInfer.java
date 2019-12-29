package com.batman.mq.consumer;

import com.batman.async.EventModel;

/**
 * @author manchaoyang
 * 2018/11/24
 */
public interface EventConsumerInfer {
    /**
     * 处理事件的函数
     *
     * @param context 事件本身
     */
    void process(EventModel context);
}
