package com.mermaid.framework.serialize;

import com.alibaba.fastjson.JSON;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/21 22:42
 * version 1.0
 */
public class JsonSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T desrialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }
}
