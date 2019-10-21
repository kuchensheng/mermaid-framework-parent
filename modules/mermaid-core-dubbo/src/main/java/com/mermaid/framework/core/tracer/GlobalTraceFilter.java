package com.mermaid.framework.core.tracer;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * ClassName:GlobalTraceFilter
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:10
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:10   kuchensheng    1.0
 */
@Activate(group = {Constants.CONSUMER,Constants.PROVIDER},order = Integer.MIN_VALUE)
public class GlobalTraceFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(GlobalTraceFilter.class);
    private static final String TRACE_ID = "traceId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = invocation.getAttachment(TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            logger.info("第一次调用，生成traceId");
            RpcContext.getContext().setAttachment("traceId", UUID.randomUUID().toString());
        } else {
            RpcContext.getContext().setAttachment("traceId",traceId);
        }
        return invoker.invoke(invocation);
    }
}
