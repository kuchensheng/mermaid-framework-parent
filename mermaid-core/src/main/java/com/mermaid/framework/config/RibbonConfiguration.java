package com.mermaid.framework.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @version 1.0
 * @Desription:开启断路器功能
 * @Author:Hui
 * @CreateDate:2018/7/1 18:19
 */
//@Configuration
//@EnableCircuitBreaker
public class RibbonConfiguration {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
