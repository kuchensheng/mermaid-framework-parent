package com.mermaid.framework.websocket.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/2/25 11:44
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if(null == is) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };
        Object object = classLoader.loadClass("com.mermaid.framework.websocket.test.ClassLoaderTest").newInstance();
        System.out.println(object.getClass());
        System.out.println(object instanceof ClassLoaderTest);
    }
}
