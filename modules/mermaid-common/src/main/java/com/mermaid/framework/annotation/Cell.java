package com.mermaid.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cell {
    /**
     * Excel中的列，从1开始
     * @return
     */
    int clumonNum();

    /**
     * 对应的列名称
     * @return
     */
    String name() default "";

    /**
     * 是否必须
     * @return
     */
    boolean required() default false;

    /**
     * 验证错误提示信息
     * @return
     */
    String errMsg() default "";

    /**
     * 正则表达式
     * @return
     */
    String regex() default "";

    int max() default Integer.MAX_VALUE;

    int min() default Integer.MIN_VALUE;

    /**
     * 取值范围
     * @return
     */
    String[] group() default "";

    /**
     * 分组标记
     * @return
     */
    boolean isGroupClumon() default false;

}
