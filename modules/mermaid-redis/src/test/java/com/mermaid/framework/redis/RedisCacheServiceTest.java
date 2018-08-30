package com.mermaid.framework.redis;

import com.mermaid.framework.ApplicationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.annotation.Resource;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/29 23:42
 * version 1.0
 */
public class RedisCacheServiceTest extends ApplicationTest{

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTest.class);

    @Resource
    private RedisService redisService;


    /**
     * 设置存储给定键的值
     * @throws Exception
     */
    @Test
    public void set() throws Exception {
        redisService.set("test","kuchensheng");
        Assert.assertTrue(true);
    }

    /**
     * 设置存储给定键的值，并设置失效时间
     * @throws Exception
     */
    @Test
    public void setWithExpire() throws Exception {
        redisService.set("testwithexpire","kucsdf",2);
        Assert.assertTrue(true);
    }

    /**
     * 获取键的集合
     * @throws Exception
     */
    @Test
    public void keys() throws Exception {
        Set<String> keys = redisService.keys("test*");
        logger.info("result={}",keys.toString());
        Assert.assertNotNull(keys);
    }

    /**
     * 获取给定键的值，默认String类型
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        String pipeline = redisService.get("test");
        logger.info("key={},value={}","test",pipeline);
        Assert.assertNotNull(pipeline);
    }

    @Test
    public void getWithClazz() throws Exception {
        String pipeline = redisService.get("test", String.class);
        Assert.assertNotNull(pipeline);
    }

    /**
     * 判断元素是否存在
     * @throws Exception
     */
    @Test
    public void exists() throws Exception {
        boolean test = redisService.exists("test");
        Assert.assertTrue(test);
    }

    /**
     * 删除键[集合]
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        redisService.delete("test","testwithexpire");
        Assert.assertTrue(true);
    }

    /**
     * 给键设置失效时间
     * @throws Exception
     */
    @Test
    public void expire() throws Exception {
        redisService.expire("test",2);
        Assert.assertTrue(true);
    }

}