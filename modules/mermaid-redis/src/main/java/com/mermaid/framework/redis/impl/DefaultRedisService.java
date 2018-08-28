package com.mermaid.framework.redis.impl;

import com.mermaid.framework.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;


@Service
public class DefaultRedisService implements RedisService {

    @Autowired
    StringRedisTemplate template;

    @Override
    public Integer set(final String key, final Object value, final long expaire) {

        Integer result = template.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = template.getStringSerializer();
                connection.setEx(serializer.serialize(key),expaire,serializer.serialize(String.valueOf(value)));
                return 1;
            }
        });
        return null;
    }

    @Override
    public Integer set(String key, Object... value) {
        return null;
    }
}
