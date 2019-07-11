package com.mermaid.framework.redis.config;

import com.mermaid.framework.redis.impl.DefaultRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/29 21:16
 * version 1.0
 */
@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport{
    @Value("${mermaid.redis.host}")
    private String host;
    @Value("${mermaid.redis.port:6379}")
    private Integer port;
    @Value("${mermaid.redis.password}")
    private String password;
    @Value("${mermaid.redis.database:0}")
    private int database;
    @Value("${mermaid.redis.pool.maxActive:8}")
    private int maxActive;
    @Value("${mermaid.redis.pool.maxWait:-1}")
    private long maxWait;
    @Value("${mermaid.redis.pool.maxIdle:8}")
    private int maxIdle;
    @Value("${mermaid.redis.timeout:8}")
    private Integer timeout;
    @Value("${mermaid.redis.pool.maxTotal:8}")
    private Integer maxTotal;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Override
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        return builder.build();
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate,redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = getJedisPoolConfig();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        if(StringUtils.hasText(password)) {
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.setDatabase(database);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }

    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory redisConnectionFactory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
    }

    @Bean
    public DefaultRedisService redisService(RedisTemplate<String,Object> redisTemplate) {
        DefaultRedisService redisService = new DefaultRedisService(DefaultRedisService.class.getSimpleName(),redisTemplate);;
        return redisService;
    }

}
