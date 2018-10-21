package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {

    private Integer actorId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityOwnerId;
    private Map<String, String> exts = new HashMap<>();
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
