# mermaid-framework-rabbitmq

# 1. 介绍
**mermaid-framework-rabbitmq**基于RabbitMQ开发，提供了RabbitMQ访问服务。其能力如下：


+ 发送队列消息（ExchangeType.DIRECT）
+ 发送订阅消息(ExchangeType.FANOUT)
+ 发送通配符消息(ExchangeType.TOPIC)
+ 自动监听队列消息
+ 主动监听队列消息(调用RabbitMQService.listen()方法)

# 2. QuickStart
```xml
<dependency>
    <groupId>com.mermaid.framework</groupId>
    <artifactId>mermaid-rabbitmq</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```