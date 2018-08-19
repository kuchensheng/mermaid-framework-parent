package com.mermaid.framework.cloud.annotation;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CloudServiceClient {

    String value() default "";
}
