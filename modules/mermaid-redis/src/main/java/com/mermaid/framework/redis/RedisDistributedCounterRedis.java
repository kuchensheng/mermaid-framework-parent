package com.mermaid.framework.redis;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/29 21:13
 * version 1.0
 */
public interface RedisDistributedCounterRedis {
    /**
     * 获取当前数量
     * @param key
     * @return
     */
    long getCurrentCounter(String key);

    /**
     * 增量,默认+1
     * @param key
     * @return
     */
    long increment(String key);

    /**
     * 指定值的增量
     * @param key
     * @param val
     * @return
     */
    long increment(String key,long val);

    /**
     * 减量，-1
     * @param key
     * @return
     */
    long decrement(String key);

    /**
     * 指定值的减量
     * @param key
     * @param val
     * @return
     */
    long decrement(String key,long val);
}
