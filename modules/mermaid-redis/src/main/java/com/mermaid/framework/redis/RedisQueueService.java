package com.mermaid.framework.redis;

/**
 * Desription:
 * Redis队列操作
 * @author:Hui CreateDate:2018/8/29 21:38
 * version 1.0
 */
public interface RedisQueueService {

    /**
     * 添加单个元素
     * @param key
     * @param value
     * @return 返回列表长度
     */
    Long push(String key,Object value);

    /**
     * 添加多个元素
     * @param key
     * @param values
     * @return 返回列表长度
     */
    Long pushAll(String key,Object... values);

    /**
     * 获取一个元素
     * @param key
     * @param <T>
     * @return 弹出一个元素
     */
    <T> T pop(String key);
}
