package com.mermaid.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/21 23:33
 * version 1.0
 */
@Component
@Primary
public class DocumentationConfiguration implements SwaggerResourcesProvider {
    
    @Autowired
    private ZuulProperties zuulProperties;

    @Override
    public List<SwaggerResource> get() {

        final List<SwaggerResource> resources = new ArrayList<>();
        for (ZuulProperties.ZuulRoute route : zuulProperties.getRoutes().values()) {
            resources.add(swaggerResource(route.getServiceId(),route.getPath().replace("**","v2/api-docs"),"1.0"));
        }

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource resource = new SwaggerResource();
        resource.setName(name);
        resource.setLocation(location);
        resource.setSwaggerVersion(version);
        return resource;
    }
}
