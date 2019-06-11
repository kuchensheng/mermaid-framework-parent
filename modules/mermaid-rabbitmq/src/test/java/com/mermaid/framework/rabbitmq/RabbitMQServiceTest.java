package com.mermaid.framework.rabbitmq;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TestApplicationContext.class)
public class RabbitMQServiceTest {

    @Autowired
    RabbitMQService rabbitMQService;

//    @Before
//    public void beforeTest() {
//        MyApplicationContext myApplicationContext = new MyApplicationContext("com.mermaid.framework.rabbitmq");
//        rabbitMQService = myApplicationContext.getBean(RabbitMQService.class);
//
//    }

//    private static class MyApplicationContext extends AnnotationConfigApplicationContext {
//        public MyApplicationContext(String basePackages) {
//            super();
//            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//            beanDefinition.setBeanClass(PropertiesDefaultHandler.class);
//            Map<String,String> map = new HashMap();
//            map.put("locations","META-INF/mermaid-framework-rabbitmq.properties");
//            beanDefinition.setPropertyValues(new MutablePropertyValues(map));
//            this.registerBeanDefinition("propertyPlaceholderConfigurer",beanDefinition);
//            scan(basePackages);
//            refresh();
//        }
//    }
    @Test
    public void sendDirectMQTest() throws InterruptedException {
        String queueName = "queue.direct";
        RabbitMQMessageTarget target = RabbitMQMessageTarget.createDirectTarget(queueName);
        rabbitMQService.send(target,"库陈胜");
        TimeUnit.SECONDS.sleep(1);
        MQTestListener mqTestListener = new MQTestListener(queueName);
        rabbitMQService.listen(mqTestListener);
        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals("库陈胜",mqTestListener.getValue());
    }

    @Test
    public void sendFanoutMqTest() throws InterruptedException {
        RabbitMQMessageTarget target = RabbitMQMessageTarget.createFanoutTarget("queue.fanout",new String[]{"queue.fanout.1","queue.fanout.2"});
        rabbitMQService.send(target,"8888");
        TimeUnit.SECONDS.sleep(1);
        MQTestListener mqTestListener1 = new MQTestListener("queue.fanout.1");
        MQTestListener mqTestListener2 = new MQTestListener("queue.fanout.2");
        rabbitMQService.listen(mqTestListener1);
        rabbitMQService.listen(mqTestListener2);
        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals("8888",mqTestListener1.getValue());
        Assert.assertEquals("8888",mqTestListener2.getValue());
    }

    @Test
    public void sendTopicMqTest() throws InterruptedException {
        RabbitMQMessageTarget target = RabbitMQMessageTarget.createTopicTarget("queue.topic","q.*","queue1","queue2","q3");
        rabbitMQService.send(target,"9999");
        TimeUnit.SECONDS.sleep(1);
        MQTestListener mqTestListener1 = new MQTestListener("queue1");
        MQTestListener mqTestListener2 = new MQTestListener("queue.fanout.2");
    }

    @Test
    public void send() throws InterruptedException {
        String queueName = "queue.default";
        rabbitMQService.send(queueName,"库陈胜");
        TimeUnit.SECONDS.sleep(1);
        MQTestListener mqTestListener = new MQTestListener(queueName);
        rabbitMQService.listen(mqTestListener);
        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals("库陈胜",mqTestListener.getValue());
    }

    @Test
    public void listen() {
    }

    @Test
    public void listen1() {
    }
}