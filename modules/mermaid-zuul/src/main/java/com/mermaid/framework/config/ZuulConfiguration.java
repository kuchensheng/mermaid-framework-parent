package com.mermaid.framework.config;

import com.mermaid.framework.filter.AccessFilter;
import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableZuulProxy
@EnableConfigurationProperties({FilterConfiguration.class})
public class ZuulConfiguration {
    @Bean
    public AccessFilter accessFilter(){
        return new AccessFilter();
    }
    @Bean
    public FilterLoader filterLoader(FilterConfiguration filterConfiguration) {
        FilterLoader filterLoader = FilterLoader.getInstance();
        filterLoader.setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
        try {
            FilterFileManager.init(
                    filterConfiguration.getInterval(),
                    filterConfiguration.getRoot()+"/pre",
                    filterConfiguration.getRoot()+"/post");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filterLoader;
    }
}
