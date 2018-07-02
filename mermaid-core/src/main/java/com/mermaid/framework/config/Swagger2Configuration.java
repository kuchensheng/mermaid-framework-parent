package com.mermaid.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/1 14:20
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    private String baskPackage;
    @Bean
    public Docket createRestApi(){
        return new Docket(
                DocumentationType.SWAGGER_2
        ).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(""))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("spring boot 利用 swagger构建api文档")
                .description("简单优雅的restful风格")
                .build();
    }

}
