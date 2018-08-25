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
@EnableFeignClients
@EnableCircuitBreaker
@PropertySource(value = {"classpath:/META-INF/mermaid-eureka.properties","classpath:/META-INF/mermaid-module-framework-core.properties"})
@ComponentScan("com")
public class MermaidApplicationEntry {

    public static void main(String[] args) {

        SpringApplication.run(MermaidApplicationEntry.class,args);


    }
}
