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

# 3. 配置解析
```text
# 自动将RabbitMessageListener类型的Bean监听到对应的队列
mermaid.framework.rabbitmq.autoListen=true
# rabbitmq服务IP
mermaid.framework.rabbitmq.host=127.0.0.1
# rabbitmq服务端口
mermaid.framework.rabbitmq.port=5672
# rabbitmq用户名
mermaid.framework.rabbitmq.userName=guest
# rabbitmq密码
mermaid.framework.rabbitmq.password=guest
# 集群
mermaid.framework.rabbitmq.addresses=
mermaid.framework.rabbitmq.vhost=/
mermaid.framework.rabbitmq.maxChannels=25
```
# 4. 示例
```java
@RestController
public class MyController {
    @Autowired
    private RabbitMQService rabbitMQService;
    
    @RequestMapping("/rabbitmq-test")
    public String rabbitMQTest() {
        return rabbitMQService.send("queue-name", "Hello");
    }
}

```
## 4.1 使用RabbitMQService
### 4.1.1 发送不指定消息类型的消息

```java
@Test
public void simpleSendTest() throws Exception{
    //发送消息，不指定ExchangeType，默认是ExchangeType.DIRECT
    rabbitService.send("q.shiguiming", 100);
    rabbitService.send("q.zhanglei", 200);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(100, MQTestListener.value);
    Assert.assertEquals(200, MQTestListener2.value);
    
}
```

### 4.1.2 发送ExchangeType.DIRECT类型的消息

```java
@Test
public void directTargetTest() throws Exception{
    RabbitMQMessageTarget mqTarget = RabbitMQMessageTarget.createDirectTarget("q.shiguiming");
    rabbitService.send(mqTarget,300);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(300, MQTestListener.value);
}
```

### 4.1.3 发送ExchangeType.FANOUT类型的消息

```java
@Test
public void fanoutTargetTest() throws Exception {
    RabbitMQMessageTarget mqTarget = RabbitMQMessageTarget.createFanoutTarget("q.fanout", new String[]{"q.fanout1","q.fanout2"});
    rabbitService.send(mqTarget, 400);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(400,MQTestListener8.value);
    Assert.assertEquals(400,MQTestListener9.value);
    rabbitService.send(mqTarget, 500);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(500,MQTestListener8.value);
    Assert.assertEquals(500,MQTestListener9.value);
}
```

### 4.1.4 发送ExchangeType.TOPIC类型的消息

```java
@Test
public void topicTargetTest() throws Exception {
    RabbitMQMessageTarget mqTarget = RabbitMQMessageTarget.createTopicTarget("q.topic", "q.routingKey", "queue1", "queue2");
    rabbitService.send(mqTarget, 400);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(400,MQTestListener8.value);
    Assert.assertEquals(400,MQTestListener9.value);
    rabbitService.send(mqTarget, 500);
    TimeUnit.SECONDS.sleep(5);
    Assert.assertEquals(500,MQTestListener8.value);
    Assert.assertEquals(500,MQTestListener9.value);
}
```
# 5.队列监听
## 5.1 自动监听
1. 使用@Component或@Service注解将消息监听器定义为Bean并确保它能被Spring容器加载
2. 确认配置项"mermaid.rabbitmq.autoListen"为"true"

```java
@Service
public class MQTestListener extends AbstractRabbitMessageListener {
    public static int value = 0;
    public MQTestListener() {
        super("q.shiguiming", 0);
    }

    /**处理监听到的消息*/
    @Override
    public void handleMessage(Object object) {
        System.out.println("MQTestListener works");
        value = (int) object;
    }
}
```

## 5.2 调用RabbitMQService.listen()方法监听
```java
@Test
public void sendTest() throws Exception{
    rabbitService.listen(new MQTestListener());
}
public class MQTestListener extends AbstractRabbitMessageListener {
    public static int value = 0;
    public MQTestListener() {
        super("q.shiguiming", 0);
    }

    @Override
    public void handleMessage(Object object) {
        System.out.println("MQTestListener works");
        value = (int) object;
    }
}
```