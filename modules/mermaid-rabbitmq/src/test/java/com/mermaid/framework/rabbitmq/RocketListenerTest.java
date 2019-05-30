package com.mermaid.framework.rabbitmq;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/30 17:53 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public class RocketListenerTest extends AbstractRocketMessageListener {

    public RocketListenerTest(String topic, String subExpression) {
        super(topic, subExpression);
    }

    @Override
    protected boolean processMessage(MessageExt msg) {
        return false;
    }
}
