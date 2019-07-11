package com.mermaid.framework;

import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.schedule.AbstractProcessor;
import com.mermaid.framework.schedule.MermaidJobProcessor;
import com.mermaid.framework.schedule.job.context.SimpleJobContext;
import com.mermaid.framework.schedule.result.ProcessResult;

public class TestProcessor extends AbstractProcessor implements MermaidJobProcessor {

    @Override
    protected void init() {

    }

    @Override
    public ProcessResult doHandle(SimpleJobContext context) {
        logger.info("received info {}",JSONObject.toJSONString(context));
        return new ProcessResult(true,1);
    }
}
