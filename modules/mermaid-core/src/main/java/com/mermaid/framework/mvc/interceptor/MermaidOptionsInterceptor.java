package com.mermaid.framework.mvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Options请求拦截器
 * @author Chensheng.Ku
 * @version 创建时间：2018/9/3 9:09
 */
public class MermaidOptionsInterceptor implements HandlerInterceptor,EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(MermaidOptionsInterceptor.class);
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        addTraceId(httpServletRequest,httpServletResponse);
        String method = httpServletRequest.getMethod();
        logger.info("请求方法REQUEST_METHOD={}",method);
        if(StringUtils.hasText(method) && RequestMethod.OPTIONS.name().equals(method)) {
            logger.info("设置响应码为{}", HttpStatus.OK.value());
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return true;
    }

    private void addTraceId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String appName = environment.getProperty("spring.application.name");
        String index = environment.getProperty("spring.application.index");
        if(!(StringUtils.isEmpty(appName) || StringUtils.isEmpty(index))) {
            long threadId = Thread.currentThread().getId();
            long timestamp = System.currentTimeMillis();
            String traceId = appName+"_"+index+"_"+threadId+"_"+timestamp;
            httpServletResponse.addHeader("traceId",traceId);
            httpServletRequest.setAttribute("traceId",traceId);
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
