package com.mermaid.framework.reflection;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/13 17:21
 */
public class AnnotationReflectUtil {
    private static Logger logger = LoggerFactory.getLogger(AnnotationReflectUtil.class);

    private static String DEFAULT_PACKAGE = "com.mermaid.framework.core";


    public static Set<Class<?>> classesAnnotationWith(String packageName, Class<? extends Annotation> annotation) {
        if(isEmpty(packageName)) {
            packageName = DEFAULT_PACKAGE;
        }
        String[] packageNames = new String[]{packageName};

        if(packageName.contains(",")) {
            packageNames = packageName.split(",");
        }

        Reflections reflections = new Reflections(packageNames);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static boolean isEmpty(String packageName) {
        return null == packageName || "".equals(packageName.trim());
    }
}
