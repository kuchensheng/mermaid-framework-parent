package com.mermaid.framework.redis;

/**
 * @author Chensheng.Ku
 * 创建时间 2018-08-29 15:56
 * 描述：Redis缓存服务接口
 */
public interface RedisCacheService {
    void set(String key,Object value);
    void set(String key,Object value,long expire);

    String[] keys(String pattern);

    String get(String key);

    boolean exists(String key);

    <T> T get(String key,Class<T> tpl);

    void delete(String... keys);

    void expire(String key ,long expire);
}
