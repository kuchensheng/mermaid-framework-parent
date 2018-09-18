package com.mermaid.framework.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/9/18 11:07
 */
public class HttpUtil {

    public static HttpServletRequest httpServletRequest() {
        return servletRequestAttributes().getRequest();
    }

    public static HttpServletResponse httpServletResponse() {
        return servletRequestAttributes().getResponse();
    }

    public static Object getSessionAttribute(String key) {
        return servletRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
    }

    public static String[] getSessionAttributeNames() {
        return servletRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_SESSION);
    }

    public static String[] getRequestAttributeNames() {
        return servletRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getRequestAttribute(String key) {
        return servletRequestAttributes().getAttribute(key,RequestAttributes.SCOPE_REQUEST);
    }

    private static ServletRequestAttributes servletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
}
