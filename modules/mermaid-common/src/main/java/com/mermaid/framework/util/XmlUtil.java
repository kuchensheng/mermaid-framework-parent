package com.mermaid.framework.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dom4j.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ClassName:XmlUtil
 * Description: 读写xml工具类
 *
 * @author: kuchensheng
 * @version: Create at:  16:50
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 16:50   kuchensheng    1.0
 */
public class XmlUtil {

    /**
     * 对象转xml，指明编码格式
     * @param object
     * @param charset
     * @return
     */
    public static String obj2Xml(Object object,String charset) {
        XStream xStream = new XStream(new DomDriver(charset));
        xStream.processAnnotations(object.getClass());
        return xStream.toXML(object);
    }

    /**
     * 对象转xml
     * @param object
     * @return
     */
    public static String obj2Xml(Object object) {
        return obj2Xml(object,"utf-8");
    }

    /**
     * xml转对象，指明编码
     * @param xmlContext
     * @param clazz
     * @param charset
     * @param <T>
     * @return
     */
    public static <T> T xml2Obj(String xmlContext,Class<T> clazz,String charset) {
        XStream xStream = new XStream(new DomDriver(charset));
        xStream.processAnnotations(clazz);
        T t = (T) xStream.fromXML(xmlContext);
        return t;
    }

    /**
     * xml转对象
     * @param xmlContext
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T xml2Obj(String xmlContext,Class<T> clazz) {
        return xml2Obj(xmlContext,clazz,"utf-8");
    }

    /**
     * 将对象写入xml文件
     * @param object
     * @param xmlPath
     * @return
     * @throws Exception
     */
    public static boolean write2XmlFile(Object object,String xmlPath) throws Exception {

        String strXml = obj2Xml(object);

        File file = new File(xmlPath);

        createFile(xmlPath);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(strXml);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != fileWriter) {
                fileWriter.close();
            }
        }
    }

    private static void createFile(String path) throws IOException {
        File file = new File(path);
        if(file.isDirectory() && !file.exists()) {
            file.mkdirs();
        } else if (file.isFile() && !file.exists()) {
            file.createNewFile();
        }
    }

}
