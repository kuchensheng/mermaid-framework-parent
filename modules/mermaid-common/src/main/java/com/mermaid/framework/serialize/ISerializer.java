package com.mermaid.framework.serialize;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/21 22:18
 * version 1.0
 */
public interface ISerializer {

    /**
     * 序列化
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T desrialize(byte[] data,Class<T> clazz);
}
