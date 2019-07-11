package com.mermaid.framework.schedule;

import com.alibaba.dts.common.domain.store.Job;
import com.alibaba.dts.common.domain.store.JobInstanceSnapshot;
import com.alibaba.edas.schedulerX.ProcessResult;
import com.alibaba.edas.schedulerX.ScxSimpleJobContext;
import com.alibaba.edas.schedulerX.ScxSimpleJobProcessor;
import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.schedule.job.MermaidJob;
import com.mermaid.framework.schedule.job.MermaidJobInstanceSnapshot;
import com.mermaid.framework.schedule.job.context.SimpleJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProcessor implements ScxSimpleJobProcessor,MermaidJobProcessor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ProcessResult process(ScxSimpleJobContext context) {
        init();

        return convertResult(doHandle(convertSimpJobContext(context)));
    }

    protected abstract void init();

    private ProcessResult convertResult(com.mermaid.framework.schedule.result.ProcessResult processResult) {
        ProcessResult result = new ProcessResult(processResult.isSuccess());
        result.setRetryCount(processResult.getRetryCount());
        result.setSleepTime(processResult.getSleeptime());
        return result;
    }

    private SimpleJobContext convertSimpJobContext(ScxSimpleJobContext scxSimpleJobContext) {
        SimpleJobContext jobContext = new SimpleJobContext();
        jobContext.setCurrentMachineNumber(scxSimpleJobContext.getCurrentMachineNumber());
        jobContext.setTask(scxSimpleJobContext.getTask());
        jobContext.setJob(converJob(scxSimpleJobContext.getJob()));
        jobContext.setTask(scxSimpleJobContext.getTask());
        jobContext.setJobInstanceSnapshot(convertJobInstanceSnapshot(scxSimpleJobContext.getJobInstanceSnapshot()));
        jobContext.setRetryCount(scxSimpleJobContext.getRetryCount());
        return jobContext;
    }

    private MermaidJob converJob(Job job) {
        String jsonString = JSONObject.toJSONString(job);
        return JSONObject.parseObject(jsonString,MermaidJob.class);
    }

    private MermaidJobInstanceSnapshot convertJobInstanceSnapshot(JobInstanceSnapshot jobInstanceSnapshot) {
        String jsonString = JSONObject.toJSONString(jobInstanceSnapshot);
        return JSONObject.parseObject(jsonString,MermaidJobInstanceSnapshot.class);
    }

}
