package com.mermaid.framework.core.config;

import com.mermaid.framework.util.MavenUtil;
import com.mermaid.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/9/7 18:20
 * version 1.0
 */
@Configuration
@EnableSwagger2
@ConditionalOnExpression("${com.mermaid.swagger2.enable:false} == true")
@PropertySource(value = "classpath:META-INF/mermaid-framework-swagger.properties",ignoreResourceNotFound = true,encoding = "UTF-8")
public class SwaggerDubboConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SwaggerDubboConfiguration.class);
    @Autowired
    private Environment environment;
    @Bean
    public Docket createRestApi(){
        String groupId = null;
        try {
            groupId = MavenUtil.getTagContent("groupId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!StringUtils.hasText(groupId)) {
            groupId = "com.mermaid";
        }
        log.info("项目的顶级pom中的groupId = {},这个将作为swagger的basePackage",groupId);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
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
