package com.mermaid.framework.core.cloud.annotation;

import org.apache.ibatis.type.Alias;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 21:48
 * version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CloudClient {

    String value();

}
