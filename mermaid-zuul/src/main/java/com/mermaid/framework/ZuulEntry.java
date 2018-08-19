package com.mermaid.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/23 21:05
 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulEntry {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulEntry.class).web(true).run(args);
    }
}
