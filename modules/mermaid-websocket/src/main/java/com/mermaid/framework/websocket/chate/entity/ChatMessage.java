package com.mermaid.framework.websocket.chate.entity;

import lombok.Data;

@Data
public class ChatMessage {

    private MessageType type;

    private String content;

    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        ;
    }


}
