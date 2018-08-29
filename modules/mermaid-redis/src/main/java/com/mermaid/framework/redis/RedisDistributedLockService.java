package com.mermaid.framework.redis;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/29 21:38
 * version 1.0
 */
public interface RedisDistributedLockService {

    /**
     * 加锁
     * @param lockName 锁名
     * @return
     */
    boolean lock(String lockName);

    /**
     * 加锁，并设置失效时间，单位：秒(seconds)
     * @param lockName
     * @param expire
     * @return
     */
    boolean lock(String lockName,long expire);

    /**
     * 解锁
     * @param lockName
     */
    void unlock(String lockName);
}
