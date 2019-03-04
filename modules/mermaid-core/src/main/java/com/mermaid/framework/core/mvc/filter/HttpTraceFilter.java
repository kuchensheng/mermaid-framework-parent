package com.mermaid.framework.core.mvc.filter;

import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.common.ThreadLocalProcessorTracer;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 19:55
 */
public class HttpTraceFilter extends AbstractTraceFilter {

    private ApplicationInfo applicationInfo;

    public HttpTraceFilter(RequestContextProcessor[] requestContextProcessors, ArrayList<String> ignoredPatterns) {
        super(requestContextProcessors, ignoredPatterns);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, ServletResponse response, FilterChain chain, ThreadLocalProcessorTracer tracer) {

    }
}
