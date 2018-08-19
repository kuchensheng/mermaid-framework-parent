package com.mermaid.framework.redis;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/8/16 23:44
 */
public interface RedisService {
    Integer set(String key,Object value,long expaire);

    Integer set(String key,Object...value);
}
