package com.mermaid.framework.core.cloud.interceptor;

import feign.RequestTemplate;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 22:41
 * version 1.0
 */
public interface InvocationInterceptor {
    void intercept(String cloudServiceName, String url, RequestTemplate requestTemplate,InvocationInterceptorChain chain);
}
