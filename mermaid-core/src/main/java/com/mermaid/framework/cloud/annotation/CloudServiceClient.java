package com.mermaid.framework.cloud.annotation;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @Desription: 注解服务调用者
 * @Author:Hui
 * @CreateDate:2018/6/22 22:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CloudServiceClient {

    String value() default "";
}
