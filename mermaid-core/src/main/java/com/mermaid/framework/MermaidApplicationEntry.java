package com.mermaid.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;


@ComponentScan({"com.mermaid.framework"})
@SpringBootApplication
public class MermaidApplicationEntry {

    public static void main(String[] args) {
        SpringApplication.run(MermaidApplicationEntry.class,args);
    }
}
