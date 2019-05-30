package com.mermaid.framework.rabbitmq.support;

import com.google.gson.Gson;
import org.apache.rocketmq.common.message.Message;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/30 14:39 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public class RocketMQMessageBuilder {
    private static Gson gson = new Gson();

    private String topic;

    private String tag;

    private String key;

    private byte[] data;

    private Integer flag;

    private Integer delaytimeLevel;

    public static RocketMQMessageBuilder create() {
        return new RocketMQMessageBuilder();
    }

    public RocketMQMessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public RocketMQMessageBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public RocketMQMessageBuilder key(String key) {
        this.key = key;
        return this;
    }

    public RocketMQMessageBuilder data(byte[] data) {
        this.data = data;
        return this;
    }

    public RocketMQMessageBuilder flag(Integer flag) {
        this.flag = flag;
        return this;
    }

    public RocketMQMessageBuilder delayTimeLevel(int delaytimeLevel) {
        this.delaytimeLevel = delaytimeLevel;
        return this;
    }

    public Message build() {
        Message message = new Message(topic,tag,key,data);
        if(null != flag) {
            message.setFlag(flag);
        }
        if(null != delaytimeLevel) {
            message.setDelayTimeLevel(delaytimeLevel);
        }

        return message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Integer getDelaytimeLevel() {
        return delaytimeLevel;
    }

    public void setDelaytimeLevel(Integer delaytimeLevel) {
        this.delaytimeLevel = delaytimeLevel;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
