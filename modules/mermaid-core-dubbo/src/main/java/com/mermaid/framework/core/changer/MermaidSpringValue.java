package com.mermaid.framework.core.changer;

import org.springframework.core.MethodParameter;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * ClassName:MermaidSpringValue
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  11:09
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 11:09   kuchensheng    1.0
 */
public class MermaidSpringValue {

    private MethodParameter methodParameter;

    private Field field;

    private WeakReference<Object> beanRef;

    private String beanName;

    private String key;

    private String placeholder;

    private Class<?> targetType;

    private Type genericType;

    private boolean isJson;

    public MermaidSpringValue(Field field, String beanName, String key, String placeholder, Class<?> targetType, boolean isJson) {
        this.field = field;
        this.beanRef = beanRef;
        this.beanName = beanName;
        this.key = key;
        this.placeholder = placeholder;
        this.targetType = targetType;
        this.isJson = isJson;
        if (isJson) {
            this.genericType = field.getGenericType();
        }
    }

    public MermaidSpringValue(String beanName, String key, boolean isJson, String placeholder, Object bean, Method method) {
        this.beanRef = new WeakReference<>(bean);
        this.beanName = beanName;
        this.methodParameter = new MethodParameter(method,0);
        this.key = key;
        this.placeholder = placeholder;
        Class<?>[] paramTps = method.getParameterTypes();
        this.targetType = paramTps[0];
        this.isJson = isJson;
        if (isJson) {
            this.genericType = method.getGenericParameterTypes()[0];
        }
    }

    public void update(Object newVal) throws IllegalAccessException, InvocationTargetException {
        if (isField()) {
            injectField(newVal);
        } else {
            injectMethod(newVal);
        }
    }

    private void injectField(Object newVal) throws IllegalAccessException {
        Object bean = beanRef.get();
        if (bean == null) {
            return;
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(bean, newVal);
        field.setAccessible(accessible);
    }

    private void injectMethod(Object newVal)
            throws InvocationTargetException, IllegalAccessException {
        Object bean = beanRef.get();
        if (bean == null) {
            return;
        }
        methodParameter.getMethod().invoke(bean, newVal);
    }

    @Override
    public String toString() {
        Object bean = beanRef.get();
        if (bean == null) {
            return "";
        }
        if (isField()) {
            return String
                    .format("key: %s, beanName: %s, field: %s.%s", key, beanName, bean.getClass().getName(), field.getName());
        }
        return String.format("key: %s, beanName: %s, method: %s.%s", key, beanName, bean.getClass().getName(),
                methodParameter.getMethod().getName());
    }

    public MethodParameter getMethodParameter() {
        return methodParameter;
    }

    public void setMethodParameter(MethodParameter methodParameter) {
        this.methodParameter = methodParameter;
    }

    public WeakReference<Object> getBeanRef() {
        return beanRef;
    }

    public void setBeanRef(WeakReference<Object> beanRef) {
        this.beanRef = beanRef;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    public Type getGenericType() {
        return genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean json) {
        isJson = json;
    }

    public boolean isField() {
        return this.field != null;
    }

    public Field getField() {
        return field;
    }
}
