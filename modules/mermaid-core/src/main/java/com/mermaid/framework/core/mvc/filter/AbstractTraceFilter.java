package com.mermaid.framework.core.mvc.filter;

import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.common.Constants;
import com.mermaid.framework.core.cloud.common.ThreadLocalProcessorTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 19:18
 */
public abstract class AbstractTraceFilter implements Filter {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractTraceFilter.class);

    private static final String[] STATIC_RESOURCE_SUFFIX={
            ".icon",".jpg",".png","gif",".swf","html",".js",".css"
    };

    private ArrayList<String> ignoredPatterns = new ArrayList<>();

    private RequestContextProcessor[] requestContextProcessors;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public AbstractTraceFilter(RequestContextProcessor[] requestContextProcessors,ArrayList<String> ignoredPatterns) {
        this.requestContextProcessors = requestContextProcessors;
        this.ignoredPatterns = ignoredPatterns;
        initializeIgnorePatterns();
    }

    private void initializeIgnorePatterns() {
        this.ignoreTrace("/druid/**")
                .ignoreTrace("/webjars/**")
                .ignoreTrace("/configuration/**")
                .ignoreTrace("/swagger-resources")
                .ignoreTrace("/v2/api-docs");
    }

    private AbstractTraceFilter ignoreTrace(String pattern) {
        if(StringUtils.hasText(pattern)) {
            ignoredPatterns.add(pattern);
        }
        return this;
    }

    private boolean isIgnoredResource(String uri) {
        for (String pattern : ignoredPatterns) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean success = true;
        ThreadLocalProcessorTracer tracer = null;
        ThreadLocalProcessorTracer.clean();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            String uri = req.getRequestURI();
            if(isIgnoredResource(uri)) {
                chain.doFilter(request,response);
                return;
            }

            //获取当前线程的process tracer, 如果HTTP HEADER中有"trace-id", 则将当前线程的tracer的traceId设置成header中的值
            String traceId = this.getTraceIdFromRequestHeader(req);
            String spanId = this.getSpanIdFromRequestHeader(req);
            if(StringUtils.hasText(traceId)) {
                tracer = ThreadLocalProcessorTracer.getNextTracer(traceId);
            }else {
                tracer = ThreadLocalProcessorTracer.get();
            }
            if(StringUtils.hasText(spanId)) {
                tracer.getSpanInfo().setParentSpanId(spanId);
            }
            parse(req,Constants.HEADER_TRACE_ID,traceId);
            parse(req,Constants.HEADER_SPAN_ID,spanId);
            doFilterInternal(req,response,chain,tracer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            success = false;
            throw e;
        } finally {
            if(!success) {
                logger.info("这里是过滤器失败的逻辑处理");
            }
            tracer.stop();
            int statusCode = resp.getStatus();
            if(needLog(statusCode)) {
                ApplicationInfo.getInstance().getCloudClient().writeRequestTraceLog();
            }
            tracer.remove();
        }
    }

    private class MutableHttpServletRequest extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        public MutableHttpServletRequest(HttpServletRequest request){
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }
    }

    public void parse(HttpServletRequest request,String key,String value) {
        new MutableHttpServletRequest(request).putHeader(key,value);
    }

    private String getSpanIdFromRequestHeader(HttpServletRequest req) {
        return req.getHeader(Constants.HEADER_SPAN_ID);
    }

    private boolean needLog(int status) {
        return status == HttpStatus.OK.value() || status == HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    protected abstract void doFilterInternal(HttpServletRequest req, ServletResponse response, FilterChain chain, ThreadLocalProcessorTracer tracer);

    private String getTraceIdFromRequestHeader(HttpServletRequest req) {
        return req.getHeader(Constants.HEADER_TRACE_ID);
    }


    @Override
    public void destroy() {

    }
}
