package com.mermaid.framework.serialization;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.0
 * @Desription:XML序列化工具
 * @Author:Hui
 * @CreateDate:2018/8/19 13:46
 */
@Slf4j
public class XmlSerializer implements ISerializer {

    //初始化XStream对象
    private static final XStream X_STREAM = new XStream(new Dom4JDriver());

    @Override
    public <T> byte[] serialize(T obj) {
        log.info("xml serializer serialize");
        return X_STREAM.toXML(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        log.info("xml serialize deserializd");
        String xml = new String(data);
        return (T) X_STREAM.fromXML(xml);
    }
}
