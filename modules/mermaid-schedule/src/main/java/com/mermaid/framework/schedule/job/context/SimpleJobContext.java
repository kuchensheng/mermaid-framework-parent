package com.mermaid.framework.schedule.job.context;

import com.alibaba.dts.client.service.JobRunningStateManager;
import com.alibaba.dts.common.domain.store.TaskSnapshot;
import com.alibaba.dts.common.util.BytesUtil;
import com.mermaid.framework.schedule.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJobContext extends MermaidJobContext implements Constants {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJobContext.class);

    /** 当前要处理的任务 */
    private Object task;

    //可用的机器数量
    private int availableMachineAmount;

    //当前机器编号
    private int currentMachineNumber;

    /**
     * 设置任务
     * @param taskSnapshot
     */
    protected void setTask(TaskSnapshot taskSnapshot) {
        if(BytesUtil.isEmpty(taskSnapshot.getBody())) {
            logger.error("[SimpleJobContext]: BytesUtil setTask bytesToObject error, body is empty"
                    + ", instanceId:" + taskSnapshot.getJobInstanceId() + ", id:" + taskSnapshot.getId());
            return ;
        }
        try {
            task = BytesUtil.bytesToObject(taskSnapshot.getBody());
        } catch (Throwable e) {
            logger.error("[SimpleJobContext]: BytesUtil setTask bytesToObject error"
                    + ", instanceId:" + taskSnapshot.getJobInstanceId() + ", id:" + taskSnapshot.getId(), e);
        }
    }

    public void updateJobRunningStatus(String status){
        JobRunningStateManager.getManageHandler().addJobRunningState(this.jobInstanceSnapshot.getId(),status);
    }

    public Object getTask() {
        return task;
    }

    public int getAvailableMachineAmount() {
        return availableMachineAmount;
    }

    protected void setAvailableMachineAmount(int availableMachineAmount) {
        this.availableMachineAmount = availableMachineAmount;
    }

    public void setTask(Object task) {
        this.task = task;
    }

    public int getCurrentMachineNumber() {
        return currentMachineNumber;
    }

    public void setCurrentMachineNumber(int currentMachineNumber) {
        this.currentMachineNumber = currentMachineNumber;
    }
}
