package com.mermaid.framework.websocket.config;

import com.mermaid.framework.websocket.declaration.WebSocketServer;
import com.mermaid.framework.websocket.netty.NettyWebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Value("${server.port:8080}")
    private String port;
    @Value("${mermaid.websocket.server.endpoint.uri:/websocket}")
    private String strUri;

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
//    @Bean
//    public WebSocketServer webSocketServer() {
//        return new WebSocketServer();
//    }

    @Bean
    public NettyWebSocketServer NettyWebSocketServer(){
        return new NettyWebSocketServer(strUri,Integer.valueOf(port));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(strUri).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/topic");
    }
}
