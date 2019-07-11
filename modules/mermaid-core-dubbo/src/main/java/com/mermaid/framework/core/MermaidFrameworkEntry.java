package com.mermaid.framework.core;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubboConfiguration
public class MermaidFrameworkEntry {
    public static void main(String[] args) {
        SpringApplication.run(MermaidFrameworkEntry.class,args);
    }
}

