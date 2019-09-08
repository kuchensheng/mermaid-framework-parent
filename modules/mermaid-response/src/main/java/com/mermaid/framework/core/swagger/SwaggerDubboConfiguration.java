package com.mermaid.framework.core.swagger;

import com.mermaid.framework.util.MavenUtil;
import com.mermaid.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Slf4j
public class SwaggerDubboConfiguration  {

    @Autowired
    private Environment environment;
    @Bean
    public Docket createRestApi(){
        Model model = MavenUtil.getMavenPomModel();
        String groupId = model.getGroupId();
        if(!StringUtils.hasText(groupId)) {
            groupId = "com.mermaid";
        }
        log.info("项目的顶级pom中的groupId = {},这个将作为swagger的basePackage",groupId);
//        String baskPackageName = environment.getProperty("mermaid.swagger.package","com.mermaid");
        log.info("swagger基础包={}",groupId);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(groupId))
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
