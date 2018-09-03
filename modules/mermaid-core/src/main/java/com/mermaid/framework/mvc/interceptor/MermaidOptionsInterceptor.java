package com.mermaid.framework.mvc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Options请求拦截器
 * @author Chensheng.Ku
 * @version 创建时间：2018/9/3 9:09
 */
public class MermaidOptionsInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MermaidOptionsInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String method = httpServletRequest.getMethod();
        logger.info("请求方法REQUEST_METHOD={}",method);
        if(StringUtils.hasText(method) && RequestMethod.OPTIONS.name().equals(method)) {
            logger.info("设置响应码为{}", HttpStatus.OK.value());
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
