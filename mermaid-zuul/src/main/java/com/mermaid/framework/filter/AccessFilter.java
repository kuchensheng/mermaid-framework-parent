package com.mermaid.framework.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/1 23:50
 */

public class AccessFilter extends ZuulFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //这里进行校验，比如身份校验
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send {} request to {}",request.getMethod(),request.getRequestURL().toString());
        String accessToken = request.getParameter("accessToken");
        if(!StringUtils.hasText(accessToken)){
            logger.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        logger.info("access token is ok");
        return null;
    }
}
