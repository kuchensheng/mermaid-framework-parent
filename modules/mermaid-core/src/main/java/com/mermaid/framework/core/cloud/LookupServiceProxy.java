package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.cloud.lookup.CloudClientLookupStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/13 20:20
 * version 1.0
 */
@Component
@Aspect
public class LookupServiceProxy{

    @Autowired
    private CloudClientLookupStrategy strategy;

    @Pointcut("@annotation(com.mermaid.framework.core.cloud.annotation.CloudClient)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void invoke2Http(ProceedingJoinPoint joinPoint) {

    }
}
