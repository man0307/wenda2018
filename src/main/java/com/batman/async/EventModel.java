package com.batman.async;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EventModel implements Serializable {

    private int serialVersionUID = 1;

    /**
     * 事件動作接收者ID
     */
    private Integer actorId;

    /**
     * 实体类型ID
     */
    private Integer entityType;

    /**
     * 实体ID
     */
    private Integer entityId;

    /**
     * 实体所有者ID
     */
    private Integer entityOwnerId;

    /**
     * 额外信息map
     */
    private Map<String, String> exts = new HashMap<>();
    /**
     * 事件类型
     */
    private EventType type;

    public EventModel() {

    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel setValue(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getValue(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public Integer getActorId() {
        return actorId;
    }

    public EventModel setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;

    }

    public Integer getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Integer getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(Integer entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }


}
