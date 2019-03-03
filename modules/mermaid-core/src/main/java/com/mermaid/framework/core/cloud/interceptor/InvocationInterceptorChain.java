package com.mermaid.framework.core.cloud.interceptor;

import feign.RequestTemplate;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 22:42
 * version 1.0
 */
public class InvocationInterceptorChain {
    protected InvocationInterceptor[] interceptors;

    protected int index;

    public InvocationInterceptorChain(InvocationInterceptor[] interceptors) {
        this.interceptors = interceptors;
        this.index = 0;
    }

    public void next(String cloudServiceName, String url, RequestTemplate requestTemplate) {
        if(this.interceptors != null && this.index < this.interceptors.length) {
            this.interceptors[this.index++].intercept(cloudServiceName, url, requestTemplate, this);
        }
    }
}
