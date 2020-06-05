package com.mermaid.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * 数据的起始行
     * @return
     */
    int startRow() default 0;

    /**
     * 设置分组列
     * @return
     */
    int groupClumnNum() default 1;
}
