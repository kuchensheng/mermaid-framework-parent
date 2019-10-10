package com.mermaid.framework.core.config;

import com.deepoove.swagger.dubbo.annotations.EnableDubboSwagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//@Component
@Configuration
@EnableDubboSwagger
@PropertySource(value = "classpath:META-INF/mermaid-framework-swagger.properties",ignoreResourceNotFound = true,encoding = "UTF-8")
public class Swagger2Configuration {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Bean
    public Docket createRestApi(){
        String baskPackageName = environment.getProperty("mermaid.swagger.package","com");
        log.info("swagger基础包={}",baskPackageName);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(baskPackageName))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(environment.getProperty("spring.application.name","mermaid-core ")+"Restful API Documentation")
                .description("RESTFUL API Documentation")
                .contact(new Contact("Chensheng.Ku",null,null))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

}
