package com.mermaid.framework.websocket.chate.listener;

import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.websocket.chate.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handlWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection,event info ={}", JSONObject.toJSONString(event));
    }

    @EventListener
    public void handleWebSocketDiscciontionListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (StringUtils.hasText(username)) {
            log.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }

}
