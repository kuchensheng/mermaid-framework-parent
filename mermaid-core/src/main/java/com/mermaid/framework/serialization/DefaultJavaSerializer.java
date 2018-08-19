package com.mermaid.framework.serialization;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @version 1.0
 * @Desription: JDK默认的Java序列化
 * 序列化时，只对对象的状态进行保存，而不管对象的方法
 * 当一个父类实现序列化，子类自动实现许雷华，不需要显示实现java.io.Serializable接口
 * 当一个对象的实例变量引用其他对象，序列化该对象时也把引用对象进行序列化
 * 当某个字段被声明为transient后，默认序列化机制就会忽略该字段
 * @Author:Hui
 * @CreateDate:2018/8/19 13:36
 */
@Slf4j
public class DefaultJavaSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        log.info("default java serialize,object must be implements java.io.serializable");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
        } catch (IOException e) {
            log.error("defaut java serialize error"+e);
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        log.info("default java deserialize");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T)objectInputStream.readObject();
        } catch (Exception e) {
            log.error("default java deserialize"+e);
            throw new RuntimeException(e);
        }
    }
}
