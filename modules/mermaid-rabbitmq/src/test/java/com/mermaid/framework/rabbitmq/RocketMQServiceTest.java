package com.mermaid.framework.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.mermaid.framework.rabbitmq.support.RocketMQMessageBuilder;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
@SpringBootTest(classes = TestApplicationContext.class)
public class RocketMQServiceTest {

    @Resource
    private RocketMQService service;

    @Test
    public void sendMessageTest() {
        Message message = RocketMQMessageBuilder.create()
                .topic("item_recal_cost_task")
                .tag("item_rcal_cost_task")
                .key("itemRecalCostTask_"+1+"_"+System.currentTimeMillis())
                .build();
        SendResult sendResult = service.sendMessage(message);
        System.out.println(JSON.toJSONString(sendResult));
        Assert.assertNotNull(sendResult);
    }

}