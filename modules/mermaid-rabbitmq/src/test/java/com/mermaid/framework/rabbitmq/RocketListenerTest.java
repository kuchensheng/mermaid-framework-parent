package com.mermaid.framework.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/30 17:53 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public class RocketListenerTest extends AbstractRocketMessageListener {

    private String topic;
    private String subExpression;

    public RocketListenerTest(DefaultMQPushConsumer mqConsumer,String topic, String subExpression) {
        super(mqConsumer,topic, subExpression);
    }

    @Override
    protected boolean processMessage(MessageExt msg) {
        System.out.println(JSON.toJSONString(msg));
        return true;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(String subExpression) {
        this.subExpression = subExpression;
    }
}
