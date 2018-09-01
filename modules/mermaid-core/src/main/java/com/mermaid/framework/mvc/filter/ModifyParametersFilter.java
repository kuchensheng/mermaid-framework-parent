package com.mermaid.framework.mvc.filter;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
public class ModifyParametersFilter extends OncePerRequestFilter {
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
