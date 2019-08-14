package com.mermaid.framework.azkaban;

import com.mermaid.framework.azkaban.modules.JobDomain;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AzkabanAdpaterTest {

    private AzkabanAdpater adpater;
    private static String projectName = "kuchensheng";
    @Before
    public void init() {
        this.adpater = new AzkabanAdpater();
    }

    @Test
    public void createProjects() {
        adpater.createProjects(projectName);
    }

    @Test
    public void createProjects1() {
        adpater.createProjects("with_description","with_description");
    }

    @Test
    public void deleteProject() {
        adpater.deleteProject("with_description");
    }

    @Test
    public void uploadZipFile() {
        adpater.uploadZip(new File("/Users/kuchensheng/Documents/job/o2o_2_hive.zip"),projectName);
    }

    @Test
    public void uploadZipFilePath() {
        adpater.uploadZip("/Users/kuchensheng/Documents/job/o2o_2_hive.zip",projectName);

    }

    @Test
    public void uploadZipWithCreateFlows() {
        List<JobDomain> jobList = new ArrayList<>(3);
        for (int i=0;i<3;i++) {
            JobDomain jobDomain = new JobDomain();
            jobDomain.setJobType(JobDomain.JobTypeEnum.COMMAND);
            jobDomain.setJobName("test-1");
            jobDomain.setCommands(new String[]{"echo \"test 11111\"" + i});
            jobList.add(jobDomain);
        }
        adpater.uploadZip("test-111",jobList,projectName);
    }

    @Test
    public void fetchProjectFlows() {
    }

    @Test
    public void fetchJobsFlow() {
    }

    @Test
    public void fetchFlowExecutions() {
    }

    @Test
    public void getRunning() {
    }

    @Test
    public void executorFlow() {
    }

    @Test
    public void executorFlow1() {
    }

    @Test
    public void cancleExecutorFlow() {
    }

    @Test
    public void scheduleFlow() {
    }

    @Test
    public void scheduleFlow1() {
    }

    @Test
    public void removeSchedule() {
    }

    @Test
    public void pauseFlow() {
    }

    @Test
    public void resumeFlow() {
    }

    @Test
    public void fetchSchecule() {
    }

    @Test
    public void removeSched() {
    }

    @Test
    public void fetchexecflow() {
    }

    @Test
    public void fetchExecJobLogs() {
    }

    @Test
    public void fetchexecflowupdate() {
    }

    @Test
    public void fetchexecflowupdate1() {
    }

    @Test
    public void fetchProjectLogs() {
    }
}