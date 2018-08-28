package com.mermaid.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@EnableZuulProxy
@SpringBootApplication
public class ZuulEntry {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulEntry.class).web(true).run(args);
    }
}
