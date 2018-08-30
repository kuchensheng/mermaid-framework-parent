package com.mermaid.framework.redis;

import com.mermaid.framework.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class RedisQueueServiceTest extends ApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTest.class);

    @Resource
    private RedisService redisService;

    @Test
    public void push() {
        Long testList = redisService.push("testList", "111");
        Assert.assertTrue(testList > 0L);
    }

    @Test
    public void pushAll() {
        Long testList = redisService.pushAll("testList",new String[]{"1","2","3","4"});
        Assert.assertTrue(testList > 0L);
    }

    @Test
    public void pop() {
        Object testList = redisService.pop("testList");
        logger.info("popo={}",String.valueOf(testList));
        Assert.assertTrue(true);
    }
}