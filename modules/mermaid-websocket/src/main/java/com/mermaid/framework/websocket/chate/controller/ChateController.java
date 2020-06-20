package com.mermaid.framework.websocket.chate.controller;

import com.mermaid.framework.websocket.chate.entity.ChatMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChateController {

    @MessageMapping(value = "/chat.send")
    @SendTo("/topic/public")
    public ChatMessage send(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping(value = "/chat.add")
    @SendTo("/topic/public")
    public ChatMessage add(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}
