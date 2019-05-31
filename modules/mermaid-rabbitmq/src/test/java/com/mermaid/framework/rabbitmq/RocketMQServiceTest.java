package com.mermaid.framework.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.mermaid.framework.rabbitmq.support.RocketMQMessageBuilder;
import com.mermaid.framework.serialize.JsonSerializer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/30 17:35 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplicationContext.class})
public class RocketMQServiceTest {

    @Autowired
    private RocketMQService service;

    @Autowired
    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Test
    public void sendMessageTest() {
        JsonSerializer serializer = new JsonSerializer();
        byte[] bytes = serializer.serialize("哈哈哈哈");
        Message message = RocketMQMessageBuilder.create()
                .topic("item_recal_cost_task")
                .tag("item_rcal_cost_task")
                .key("itemRecalCostTask_"+1+"_"+System.currentTimeMillis())
                .data(bytes)
                .build();
        SendResult sendResult = service.sendMessage(message);
        System.out.println(JSON.toJSONString(sendResult));
        RocketMessageListener rocketMessageListener = new RocketListenerTest(defaultMQPushConsumer,"item_recal_cost_task",null);
        service.listen(rocketMessageListener);
        while (true) {
            try {
                System.out.println("休息5s");
                TimeUnit.SECONDS.sleep(5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Assert.assertNotNull(sendResult);
    }

    @Test
    public void listenerTest() {
        RocketMessageListener rocketMessageListener = new RocketListenerTest(defaultMQPushConsumer,"item_recal_cost_task",null);
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}