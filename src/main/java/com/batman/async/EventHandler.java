package com.batman.async;

import java.util.List;

/**
 * 弃用
 * 现在项目使用RabbitMQ做消息队列
 * 因为是自己的小项目 所以代码不做删除
 */
public interface EventHandler {
    /**
     * 事件的处理函数
     * @param eventModel
     */
    public void doHandler(EventModel eventModel);

    /**
     * 事件类型支持函数
     * @return
     */
    public List<EventType> getEventHandlerSupport();
}
