package com.batman.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yehuo
 */
@Data
public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private Date createdDate;

    private Integer hasRead;

    private String conversationId;

    private String content;

    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        }
        return String.format("%d_%d", toId, fromId);
    }
}