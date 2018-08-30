package com.mermaid.framework.redis;

import com.mermaid.framework.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class RedisDistributedLockServiceTest extends ApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTest.class);

    @Resource
    private RedisService redisService;

    @Test
    public void lock() {
        boolean testLock = redisService.lock("testLock");
        Assert.assertTrue(testLock);
    }

    @Test
    public void lockwithExisted() {
        boolean testLock = redisService.lock("testLock",1);
        Assert.assertTrue(true);
    }

    @Test
    public void lockwithNotExisted() {
        boolean testLock = redisService.lock("testLock1",1);
        Assert.assertTrue(testLock);
    }

    @Test
    public void unlock() {
        redisService.unlock("testLock");
        Assert.assertTrue(true);
    }
}