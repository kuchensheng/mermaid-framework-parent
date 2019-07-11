package com.mermaid.framework.schedule;

import com.mermaid.framework.schedule.job.context.SimpleJobContext;
import com.mermaid.framework.schedule.result.ProcessResult;

public interface MermaidJobProcessor {

    ProcessResult doHandle(SimpleJobContext context);
}
