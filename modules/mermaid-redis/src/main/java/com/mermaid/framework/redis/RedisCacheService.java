package com.mermaid.framework.redis;

import java.util.Set;

/**
 * @author Chensheng.Ku
 * 创建时间 2018-08-29 15:56
 * 描述：Redis缓存服务接口
 */
public interface RedisCacheService {
    /**
     * 设置存储在给定键中的值
     * @param key
     * @param value
     */
    void set(String key,Object value);

    /**
     * 设置存储在给定键中的值，并设置失效时间
     * @param key
     * @param value
     * @param expire
     */
    void set(String key,Object value,long expire);

    /**
     * 获取键的集合
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 获取给定键的字符串值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取给定键的值，并自动转化为指定对象
     * @param key
     * @param tpl
     * @param <T>
     * @return
     */
    <T> T get(String key,Class<T> tpl);

    /**
     * 判断给定键是否存在
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 删除键
     * @param keys
     */
    void delete(String... keys);

    /**
     * 设置失效时间
     * @param key
     * @param expire 失效时间，单位：秒（seconds）
     */
    void expire(String key ,long expire);
}
