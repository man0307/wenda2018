package com.batman.model;

import lombok.Data;

/**
 *
 * @author manchaoyang
 * @date 2016/7/9
 */
public enum EntityType {

    ENTITY_QUESTION(1),
    ENTITY_COMMENT(2),
    ENTITY_USER(3);

    private int code;

    EntityType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
