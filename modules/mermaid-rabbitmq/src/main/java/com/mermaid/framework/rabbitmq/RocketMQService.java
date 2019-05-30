package com.mermaid.framework.rabbitmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/29 17:31 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public interface RocketMQService {

    /**
     * @param topic 主题
     * @param data 消息内容
     * @Return
     * @Decription:  消息发布
     * @CreateDate: Created in 2019/5/30 10:11
     * @Author: <a href="kuchensheng@quannengzhanggui.cn">kucs</a>
     * @Modify:
     */
    SendResult sendMessage(String topic,byte[] data);

    /**
     * @param message 消息体 
     * @Return 发送结果 
     * @Decription:  消息发送
     * @CreateDate: Created in 2019/5/30 17:26
     * @Author: <a href="kuchensheng@quannengzhanggui.cn">kucs</a> 
     * @Modify:  
     */
    SendResult sendMessage(Message message);

    /**
     * @param timeout 消息发送超时时间 
     * @Return  
     * @Decription:  
     * @CreateDate: Created in 2019/5/30 17:27
     * @Author: <a href="kuchensheng@quannengzhanggui.cn">kucs</a> 
     * @Modify:  
     */
    SendResult sendMessage(Message message,long timeout);

    /**
     * @param listener 监听器 
     * @Return  
     * @Decription:  
     * @CreateDate: Created in 2019/5/30 17:27
     * @Author: <a href="kuchensheng@quannengzhanggui.cn">kucs</a> 
     * @Modify:  
     */
    void listen(RocketMessageListener listener);
}
