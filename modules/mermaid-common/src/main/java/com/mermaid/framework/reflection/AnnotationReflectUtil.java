package com.mermaid.framework.reflection;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/13 17:21
 */
public class AnnotationReflectUtil {
    private static Logger logger = LoggerFactory.getLogger(AnnotationReflectUtil.class);

    private static String DEFAULT_PACKAGE = "com.mermaid.framework.core";


    /**
     * 获取指定包下所有的被指定注解注解的类的集合
     * @param packageName
     * @param annotation
     * @return
     */
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

    /**
     * 判断clazz是否被annotation注解
     * @param clazz
     * @param annotiaon
     * @return
     */
    public static boolean classIsAnnotationWith(Class<?> clazz,Class<? extends Annotation> annotiaon) {
        return null != clazz.getAnnotation(annotiaon);
    }

    public static <T extends Annotation> T[] getAnnotations(Class<?> clazz) {
        return (T[]) clazz.getAnnotations();
    }

    public static <T extends Annotation> T[] getMethodAnnotations(Class<?> clazz,String methodName,Class<?>... parameterTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return (T[]) method.getAnnotations();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends Annotation> T getMethodAnnotation() {
        return null;
    }
    public static boolean isEmpty(String packageName) {
        return null == packageName || "".equals(packageName.trim());
    }
}
