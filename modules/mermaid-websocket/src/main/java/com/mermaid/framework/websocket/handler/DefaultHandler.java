package com.mermaid.framework.websocket.handler;

import com.mermaid.framework.websocket.constant.EnumMessageType;
import com.mermaid.framework.websocket.declaration.AbstractMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultHandler extends AbstractMessageHandler {
    @Override
    public void handlerMessage(EnumMessageType messageType, String message) {
      log.info(message);
    }

    @Override
    public Object handlerMessageWithReturn(EnumMessageType messageType, String message) {
        log.info(message);
        return message;
    }
}
