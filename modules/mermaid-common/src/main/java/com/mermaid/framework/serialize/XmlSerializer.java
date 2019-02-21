package com.mermaid.framework.serialize;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/21 22:41
 * version 1.0
 */
public class XmlSerializer implements ISerializer {
    private static volatile XStream xStream = null;

    @Override
    public <T> byte[] serialize(T obj) {
        return getxStream().toXML(obj).getBytes();
    }

    @Override
    public <T> T desrialize(byte[] data, Class<T> clazz) {
        return (T) getxStream().fromXML(new String(data));
    }

    private XStream getxStream() {
        if(null == xStream) {
            synchronized (XmlSerializer.class) {
                if(null == xStream) {
                    xStream = new XStream(new DomDriver());
                }
            }
        }
        return xStream;
    }
}
