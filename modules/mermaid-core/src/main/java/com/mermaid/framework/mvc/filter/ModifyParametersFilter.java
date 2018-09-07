package com.mermaid.framework.mvc.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 14:35
 * version 1.0
 */
@Configuration
@Order(Integer.MIN_VALUE)
public class ModifyParametersFilter extends OncePerRequestFilter {

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ModifyParametersFilter());
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(1);
        registrationBean.setName("modifyParametersFilter");
        return registrationBean;
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new HttpPutFormContentFilter();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ModifyParametersWrapper modifyParametersWrapper = new ModifyParametersWrapper(request);
        String token = request.getHeader("token");
        String ua = request.getHeader("User-Agent");
        if(StringUtils.hasText(token)) {
            modifyParametersWrapper.putHeader("Cookie","JESSIONID="+token);
        }
        String lastReqTime = request.getHeader("currentReqTime");
        modifyParametersWrapper.putHeader("lastReqTime",lastReqTime);
        String currentTime = String.valueOf(System.currentTimeMillis());
        modifyParametersWrapper.putHeader("currentReqTime",currentTime);
        Environment environment = getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("spring.application.index");
        String traceId = appName+"-"+port+"-"+currentTime;
        modifyParametersWrapper.putHeader("traceId",traceId);

        response.setHeader("currentReqTime",currentTime);
        response.setHeader("traceId",traceId);
        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
        if(RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        filterChain.doFilter(modifyParametersWrapper,response);
    }

    private class ModifyParametersWrapper extends HttpServletRequestWrapper{
        private Map<String,String> paramMap;
        public ModifyParametersWrapper(HttpServletRequest request) {
            super(request);
            paramMap = new HashMap<>();
        }


        public void putHeader(String key, String value) {
            this.paramMap.put(key,value);
        }

        @Override
        public String getHeader(String name) {
            String HeaderValue = paramMap.get(name);
            if(StringUtils.hasText(HeaderValue)) {
                return HeaderValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Enumeration<String> headerNames = super.getHeaderNames();
            Set<String> set = new HashSet<>(paramMap.keySet());
            while (headerNames.hasMoreElements()) {
                String node = headerNames.nextElement();
                set.add(node);
            }
            return Collections.enumeration(set);
        }
    }
}
