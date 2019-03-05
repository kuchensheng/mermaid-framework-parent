package com.mermaid.framework.rabbitmq;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 10:23
 */
public interface ConcurrentRabbitMessageListener extends RabbitMessageListener{
    int getConcurrent();
}
