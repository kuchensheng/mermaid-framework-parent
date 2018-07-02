package com.mermaid.framework;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/1 13:37
 */
@SpringBootApplication
@EnableEurekaServer
public class MermaidEurekaServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MermaidEurekaServer.class).web(true).run(args);
    }
}
