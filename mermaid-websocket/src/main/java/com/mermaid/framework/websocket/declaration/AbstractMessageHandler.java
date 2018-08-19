package com.mermaid.framework.websocket.declaration;

import com.mermaid.framework.websocket.constant.EnumMessageType;


public abstract class AbstractMessageHandler {

    public abstract void handlerMessage(EnumMessageType messageType,String message);

    public abstract Object handlerMessageWithReturn(EnumMessageType messageType,String message);
}
