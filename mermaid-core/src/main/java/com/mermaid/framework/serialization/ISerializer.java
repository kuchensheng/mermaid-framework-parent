package com.mermaid.framework.serialization;

/**
 * version 1.0
 * Desription: 序列化通用服务
 * @author :Hui
 * CreateDate:2018/8/19 13:33
 */
public interface ISerializer {

    /**
     * 序列化
     * @param obj
     * @param <T>
     * @return
     */
    public <T> byte[] serialize(T obj);

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deserialize(byte[] data,Class<T> clazz);
}
