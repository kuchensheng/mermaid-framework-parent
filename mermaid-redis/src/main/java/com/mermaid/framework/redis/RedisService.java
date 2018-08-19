package com.mermaid.framework.redis;


public interface RedisService {
    Integer set(String key,Object value,long expaire);

    Integer set(String key,Object...value);
}
