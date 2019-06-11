package com.mermaid.framework.rabbitmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/29 17:29 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public interface RocketMessageListener extends MessageListenerConcurrently {

    String getListenerTopic();
}
