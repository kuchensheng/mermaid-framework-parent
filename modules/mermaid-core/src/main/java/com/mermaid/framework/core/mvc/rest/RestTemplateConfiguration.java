package com.mermaid.framework.core.mvc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 22:46
 * version 1.0
 */
@Configuration
public class RestTemplateConfiguration {

    @Value("${marmaid.framework.core.rest.invacation.connectionTimeout:3000}")
    private int restTemplateConnectTimeout;
    @Value("${marmaid.framework.core.rest.invacation.restTemplateReadTimeout:2000}")
    private int restTemplateReadTimeout;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(restTemplateConnectTimeout);
        simpleClientHttpRequestFactory.setReadTimeout(restTemplateReadTimeout);
        return simpleClientHttpRequestFactory;
    }


    @Autowired
    @Bean
    public RestTemplate  restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {

        return new TracableRestTemplate(clientHttpRequestFactory);
    }
}
