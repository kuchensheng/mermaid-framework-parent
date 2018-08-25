package com.mermaid.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.StringUtils;

import java.util.Properties;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com"})
@PropertySource(value = {"classpath:/META-INF/mermaid-eureka.properties","classpath:/META-INF/mermaid-module-framework-core.properties"})
@ComponentScan(basePackages = {"com"})
public class MermaidApplicationEntry {

    public static void main(String[] args) {
        System.setProperty("eureka.instance.prefer-ip-address",Boolean.TRUE.toString());
        System.setProperty("eureka.instance.instance-id","${spring.cloud.client.ipAddress}:${spring.application.index}");
        System.setProperty("eureka.instance.hostname","${spring.cloud.client.ipAddress}");
        System.setProperty("eureka.instance.non-secure-port","${spring.application.index}");

        SpringApplication.run(MermaidApplicationEntry.class,args);


    }
}
