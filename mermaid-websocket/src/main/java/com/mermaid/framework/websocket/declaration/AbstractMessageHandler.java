package com.mermaid.framework.websocket.declaration;

import com.mermaid.framework.websocket.constant.EnumMessageType;

/**
 * @version 1.0
 * @Desription: 消息处理顶级类
 * @Author:Hui
 * @CreateDate:2018/6/23 22:27
 */
public abstract class AbstractMessageHandler {

    public abstract void handlerMessage(EnumMessageType messageType,String message);

    public abstract Object handlerMessageWithReturn(EnumMessageType messageType,String message);
}
