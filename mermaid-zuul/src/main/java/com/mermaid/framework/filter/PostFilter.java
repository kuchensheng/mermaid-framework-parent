package com.mermaid.framework.filter;

import com.netflix.zuul.ZuulFilter;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/2 0:09
 */
public class PostFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        return null;
    }
}
