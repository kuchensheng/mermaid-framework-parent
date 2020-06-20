package com.mermaid.framework.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(scanBasePackages = {"com.mermaid.framework.websocket"})
@EnableWebSocket
public class ChatEntry {

    public static void main(String[] args) {
        SpringApplication.run(ChatEntry.class,args);
    }
}
