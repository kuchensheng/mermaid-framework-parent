package com.mermaid.framework.core.mvc.filter;

import com.mermaid.framework.core.cloud.common.ThreadLocalProcessorTracer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-04 20:01
 * 描述：
 */
public interface RequestContextProcessor {
    void beforeRequest(HttpServletRequest request, HttpServletResponse response, ThreadLocalProcessorTracer tracer);

    void afterRequest(HttpServletRequest request,HttpServletResponse response,ThreadLocalProcessorTracer tracer);
}
