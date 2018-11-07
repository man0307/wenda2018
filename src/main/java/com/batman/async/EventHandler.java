package com.batman.async;

import java.util.List;

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
