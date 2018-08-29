package com.mermaid.framework.redis.impl;

import com.mermaid.framework.redis.RedisCacheService;
import com.mermaid.framework.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;


public class DefaultRedisService implements RedisCacheService {

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public void set(String key, Object value, long expire) {

    }

    @Override
    public String[] keys(String pattern) {
        return new String[0];
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public <T> T get(String key, Class<T> tpl) {
        return null;
    }

    @Override
    public void delete(String... keys) {

    }

    @Override
    public void expire(String key, long expire) {

    }
}
