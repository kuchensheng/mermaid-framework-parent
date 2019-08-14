package com.mermaid.framework.azkaban;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mermaid.framework.azkaban.job.JobTemplate;
import com.mermaid.framework.azkaban.modules.JobDomain;
import com.mermaid.framework.util.SSLUtils;
import com.mermaid.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/26 20:58
 * version 1.0
 */
public class AzkabanAdpater {
    private static final Logger logger = LoggerFactory.getLogger(AzkabanAdpater.class);

    private String uri;

    private String userName;

    private String password;

    private String commonShPath;

    private static RestTemplate restTemplate = restTemplate();

    private static HttpHeaders hs = buildHttpHeaders();

    private static volatile String sessionId = null;

    private static RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(2000);
        return new RestTemplate(requestFactory);
    }

    private static HttpHeaders buildHttpHeaders() {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");
        disableChecks();
        return hs;
    }

    public AzkabanAdpater() {
        login();
    }

    public AzkabanAdpater(String uri, String userName, String password) {
        this.uri = uri;
        this.userName = userName;
        this.password = password;
        login(uri,userName,password);
    }

    /**
     * 登陆azkaban
     * @throws Exception
     */
    private void login(){
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/azkaban.properties");
            properties.load(resourceAsStream);
            if(null == uri) {
                this.uri = properties.getProperty("mermaid.azkaban.uri");
            }
            if(null == userName) {
                this.userName = properties.getProperty("mermaid.azkaban.username");
            }
            if(null == password) {
                this.password = properties.getProperty("mermaid.azkaban.password");
            }
            this.commonShPath = properties.getProperty("mermaid.azkaban.home","/home/test/data/");
            login(uri,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆azkaban
     * @param uri azkaban 地址
     * @param userName 用户名
     * @param password 密码
     * @throws Exception 登陆异常信息
     */
    private void login(String uri,String userName,String password) {
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        linkedMultiValueMap.add("action", "login");
        linkedMultiValueMap.add("username", userName);
        linkedMultiValueMap.add("password", password);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
        String responsResultString = restTemplate.postForObject(uri, httpEntity, String.class);
        responsResultString = responsResultString.replace(".", "_");
        JSONObject resJson = JSON.parseObject(responsResultString);
        if (resJson.getString("status").equals("success")) {
            sessionId = resJson.getString("session_id");
            logger.info("azkaban login success:{}", resJson);
        } else {
            logger.warn("azkabna login failure:{}", resJson);
        }
        this.uri = uri;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 创建项目
     * @param projectName 项目名称
     * @return 创建结果 json字符串
     */
    public String createProjects(String projectName) {
        logger.info("create azkaban project【name={}】",projectName);
        return createProjects(projectName,projectName);
    }

    /**
     * 创建项目
     * @param projectName 项目名
     * @param description 描述
     * @return 创建结果 json字符串
     */
    public String createProjects(String projectName, String description) {
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        if(null == sessionId) {
            login();
        }
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("action", "create");
        linkedMultiValueMap.add("name", projectName);
        linkedMultiValueMap.add("description", description);
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
        deleteProject(projectName);
        String res = restTemplate.postForObject(uri + "/manager", httpEntity, String.class);
        logger.info("azkaban create project failure:{}",res);
        return res;
    }

    /**
     * 删除项目
     * @param projectName 项目名
     * @return 删除结果
     */
    public boolean deleteProject(String projectName) {
        logger.info("删除工程={}",projectName);
        logger.warn("请注意，这不是彻底删除！！！");
        if(null == sessionId) {
            login();
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", sessionId);
        map.put("project", projectName);
        ResponseEntity<String> exchange = restTemplate.exchange(this.uri + "/manager?session.id={id}&delete=true&project={project}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        logger.info("azkaban delete project:{}", 200 == exchange.getStatusCodeValue());
        return 200 == exchange.getStatusCodeValue();
    }

    public String uploadZip(File zipFile,String projectName) {
        if(null == sessionId) {
            login();
        }
        FileSystemResource resource = new FileSystemResource(zipFile);
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "upload");
        linkedMultiValueMap.add("project", projectName);
        linkedMultiValueMap.add("file", resource);
        String res = restTemplate.postForObject(uri + "/manager", linkedMultiValueMap, String.class);
        logger.info("azkaban upload zip:{}", res);
        //TODO 删除zip
//        zipFile.delete();
        return res;
    }

    /**
     * 文件上传
     * @param zipFilePath zip文件
     * @param projectName 项目名
     * @return 返回操作结果 json串
     * {
    "error" : "Installation Failed.\nError unzipping file.",
    "projectId" : "192",
    "version" : "1"
    }
     */
    public String uploadZip(String zipFilePath, String projectName) {
        return uploadZip(new File(zipFilePath),projectName);
    }

    /**
     * 上传工作流
     * @param flowName 流名称
     * @param jobList 该流对应的作业信息
     * @param projecteName 工程名
     * @return
     */
    public String uploadZip(String flowName, List<JobDomain> jobList, String projecteName) {
        logger.info("根据配置信息创建job文件和对应的zip文件，并上传");
        if(CollectionUtils.isEmpty(jobList)) {
            return null;
        }

        Map<String,String> map = new HashMap<>();
        for (JobDomain value : jobList) {
            // 创建job
            logger.info("创建Job【name={}】",value.getJobName());
            map.put(value.getJobName(),JobTemplate.createCommand(this.commonShPath,value.getJobName(),
                    value.getCommands(),value.getCallbackJobId(),value.getDependencies()));
        }
        return uploadZip(JobTemplate.createZipFile(flowName,map),projecteName);
    }

    /**
     * 查询项目的flow
     * @param projectName 项目名
     * @return flow信息
     * {
    "project" : "projectName",
    "projectId" : 192,
    "flows" : [ {
    "flowId" : "test"
    }, {
    "flowId" : "test2"
    } ]
    }
     */
    public String fetchProjectFlows(String projectName) {
        logger.info("查询项目的流信息，projectName={}",projectName);
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/manager?ajax=fetchprojectflows&session.id={1}&project={2}"
                        , String.class, sessionId, projectName
                );
        logger.info("azkban fetch project flows:{}", res);
        return res;
    }

    /**
     * 获取流的作业,in 表示依赖的上级job
     * @param projectName 项目名
     * @param flowId 流的id {@See fetchProjectFlows(projectName)}
     * @return
     *
     * {
    "project" : "azkaban-test-project",
    "nodes" : [ {
    "id" : "test-final",
    "type" : "command",
    "in" : [ "test-job-3" ]
    }, {
    "id" : "test-job-start",
    "type" : "java"
    }, {
    "id" : "test-job-3",
    "type" : "java",
    "in" : [ "test-job-2" ]
    }, {
    "id" : "test-job-2",
    "type" : "java",
    "in" : [ "test-job-start" ]
    } ],
    "flow" : "test",
    "projectId" : 192
    }
     */
    public String fetchJobsFlow(String projectName,String flowId) {
        logger.info("获取流【projectName={},flowId={}】的相关信息和依赖",projectName,flowId);
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/manager?ajax=fetchflowgraph&session.id={1}&project={2}&flow={3}"
                        , String.class, sessionId, projectName,flowId
                );
        logger.info("azkban fetch Jobs of flow:{}", res);
        return res;
    }

    /**
     * 获取流的执行列表
     * @param projectName 项目名
     * @param flowId 流名称/ID
     * @param start 开始位置
     * @param length 返回的个数，用于分页
     * @return
     *
     * {
     *   "executions" : [ {
     *     "startTime" : 1407779928865,
     *     "submitUser" : "1",
     *     "status" : "FAILED",
     *     "submitTime" : 1407779928829,
     *     "execId" : 306,
     *     "projectId" : 192,
     *     "endTime" : 1407779950602,
     *     "flowId" : "test"
     *   }, {
     *     "startTime" : 1407779877807,
     *     "submitUser" : "1",
     *     "status" : "FAILED",
     *     "submitTime" : 1407779877779,
     *     "execId" : 305,
     *     "projectId" : 192,
     *     "endTime" : 1407779899599,
     *     "flowId" : "test"
     *   }, {
     *     "startTime" : 1407779473354,
     *     "submitUser" : "1",
     *     "status" : "FAILED",
     *     "submitTime" : 1407779473318,
     *     "execId" : 304,
     *     "projectId" : 192,
     *     "endTime" : 1407779495093,
     *     "flowId" : "test"
     *   } ],
     *   "total" : 16,
     *   "project" : "azkaban-test-project",
     *   "length" : 3,
     *   "from" : 0,
     *   "flow" : "test",
     *   "projectId" : 192
     * }
     */
    public String fetchFlowExecutions(String projectName,String flowId,Integer start,Integer length) {
        logger.info("获取流的执行信息列表，可查看对应的执行时间和状态，projectName={},flowId={}",projectName,flowId);
        if(null == sessionId) {
            login();
        }

        String res = restTemplate.getForObject(uri +"/manager?ajax=fetchFlowExecutions&session.id={1}&project={2}&flow={3}&start={4}&length={5}",String.class,
                sessionId,projectName,flowId,null == start ? 0 : start,length);
        logger.info("azkban fetch project flows:{}", res);
        return res;
    }

    /**
     * 获取正在运行的任务列表
     * @param projectName
     * @param flowId
     * @return
     * {
    "execIds": [301, 302]
    }
     */
    public String getRunning(String projectName,String flowId) {
        logger.info("获取正在执行的任务列表，projectName={},flowId={}",projectName,flowId);
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/executor?ajax=getRunning&session.id={1}&project={2}&flow={3}"
                        , String.class, sessionId, projectName,flowId
                );
        logger.info("azkban executions that are currently running:{}", res);
        return res;
    }

    /**
     * 执行Flow
     * @param projectName
     * @param flowId
     * @return
     * {
    message: "Execution submitted successfully with exec id 295",
    project: "foo-demo",
    flow: "test",
    execid: 295
    }
     */
    public String executorFlow(String projectName,String flowId) {
        return executorFlow(projectName,flowId,null);
    }

    /**
     * 执行flow，其中不执行某些节点
     * @param projectName
     * @param flowId
     * @param disableExectionIds
     * @return
     * {
    message: "Execution submitted successfully with exec id 295",
    project: "foo-demo",
    flow: "test",
    execid: 295
    }
     */
    public String executorFlow(String projectName,String flowId,String[] disableExectionIds ) {
        logger.info("执行流，projectName={},flowId={},需要剔除的节点信息={}",projectName,flowId,disableExectionIds);
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "executeFlow");
        linkedMultiValueMap.add("project", projectName);
        linkedMultiValueMap.add("flow", flowId);
        if(null != disableExectionIds && disableExectionIds.length > 0) {
            linkedMultiValueMap.add("disabled",disableExectionIds);
        }

        String res = restTemplate().postForObject(uri + "/executor", linkedMultiValueMap,String.class);
        return res;
    }

    /**
     * 取消执行flow
     * @param execId exceid
     * @return
     */
    public boolean cancleExecutorFlow(String execId) {
        logger.info("取消流任务的执行");
        String res = restTemplate
                .getForObject(uri + " /executor?ajax=cancelFlow&session.id={1}&execid={2}"
                        , String.class, sessionId,execId
                );
        logger.info("azkban executions that are currently running:{}", res);
        return !res.contains("error");
    }

    /**
     * 定时执行 flow
     * @param projectName 工程名
     * @param flowId 流名称/ID
     * @param startTime 开始执行时间
     * @param period 周期
     * @param periodEnum 周期单位
     * @return
     */
    public String scheduleFlow(String projectName, String flowId, LocalDateTime startTime, Integer period, PeriodEnum periodEnum) {
        LocalDate localDate = startTime.toLocalDate();
        LocalTime localTime = startTime.toLocalTime();
        DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //"11,14,AM,+08:00","08/01/2019"
        DateTimeFormatter.ofPattern("HH,mm");

        String scheduleDate = localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String scheduleTime = localTime.format(DateTimeFormatter.ofPattern("HH,mm"));

        int hour = localTime.getHour();
        if (hour >=12 ) {
            scheduleTime += ",PM";
        } else {
            scheduleTime += ",AM";
        }
        scheduleTime += ",+8:00";

        return scheduleFlow(projectName,flowId,scheduleTime,scheduleDate,period,periodEnum);
    }
    /**
     * 定时执行任务
     * @param projectName 工程名
     * @param flowName 流名称/ID
     * @param scheduleTime 开始时间
     * @param scheduleDate 开始日期
     * @param period 周期
     * @param periodEnum 周期单位
     * @return
     */
    private String scheduleFlow(String projectName,String flowName,String scheduleTime,String scheduleDate, Integer period,PeriodEnum periodEnum) {
        logger.info("定时执行任务，projectName={},flowName={},开始时间={},开始日期={},周期={},",projectName,flowName,scheduleTime,scheduleDate,period+periodEnum.value);
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
//        linkedMultiValueMap.add("ajax", "scheduleFlow");
        linkedMultiValueMap.add("projectName", projectName);
        String s = fetchProjectFlows(projectName);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String projectId = jsonObject.getString("projectId");
        linkedMultiValueMap.add("projectId",projectId);
        linkedMultiValueMap.add("flow", flowName);
        linkedMultiValueMap.add("is_recurring","on");
        if (!StringUtils.isEmpty(scheduleTime)) {
            linkedMultiValueMap.add("scheduleTime",scheduleTime);

        }
        if (!StringUtils.isEmpty(scheduleDate)) {
            linkedMultiValueMap.add("scheduleDate",scheduleDate);
        }

        if(null != period && null != periodEnum) {
            linkedMultiValueMap.add("period",period + periodEnum.getValue());
        }

        String res = restTemplate.postForObject(uri + "/schedule?ajax=scheduleFlow",linkedMultiValueMap,String.class);
        logger.info("schedule flow result = {}",res);
        return res;
    }

    /**
     * 定时执行任务
     * cron表达式
     * @param projectName 工程名
     * @param flowName 流名称
     * @param cron cron表达式
     * @return
     * {
    "message" : "PROJECT_NAME.FLOW_NAME scheduled.",
    "status" : "success"
    }
     */
    public String scheduleFlow(String projectName,String flowName,String cron) {
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "scheduleCronFlow");
        linkedMultiValueMap.add("projectName", projectName);
        linkedMultiValueMap.add("flow", flowName);
        linkedMultiValueMap.add("cronExpression",cron);

        return restTemplate.postForObject(uri + "/schedule",linkedMultiValueMap,String.class);
    }

    /**
     * 取消定时任务
     * @param scheduleId
     * @return
     */
    public Boolean removeSchedule(String scheduleId) {
        String res = restTemplate
                .getForObject(uri + " /executor?ajax=removeSched&session.id={1}&scheduleId={2}"
                        , String.class, sessionId,scheduleId
                );
        logger.info("azkban executions that are currently running:{}", res);
        return "success".equals(JSONObject.parseObject(res).getString("status"));
    }

    /**
     * 暂停流执行
     * @param execId
     * @return
     */
    public Boolean pauseFlow(String execId) {
        String res = restTemplate
                .getForObject(uri + " /executor?ajax=pauseFlow&session.id={1}&execid={2}"
                        , String.class, sessionId,execId
                );
        logger.info("azkban pauseFlow that are currently running:{}", res);
        return !res.contains("error");
    }

    /**
     * 恢复流执行
     * @param execId
     * @return
     */
    public Boolean resumeFlow(String execId) {
        String res = restTemplate
                .getForObject(uri + " /executor?ajax=resumeFlow&session.id={1}&execid={2}"
                        , String.class, sessionId,execId
                );
        logger.info("azkban resumeFlow that are currently running:{}", res);
        return !res.contains("error");
    }



    /**
     * 查看定时任务信息
     * @param projectName
     * @param flowId
     * @return
     * {
     *   "schedule" : {
     *     "cronExpression" : "0 * 9 ? * *",
     *     "nextExecTime" : "2017-04-01 09:00:00",
     *     "period" : "null",
     *     "submitUser" : "azkaban",
     *     "executionOptions" : {
     *       "notifyOnFirstFailure" : false,
     *       "notifyOnLastFailure" : false,
     *       "failureEmails" : [ ],
     *       "successEmails" : [ ],
     *       "pipelineLevel" : null,
     *       "queueLevel" : 0,
     *       "concurrentOption" : "skip",
     *       "mailCreator" : "default",
     *       "memoryCheck" : true,
     *       "flowParameters" : {
     *       },
     *       "failureAction" : "FINISH_CURRENTLY_RUNNING",
     *       "failureEmailsOverridden" : false,
     *       "successEmailsOverridden" : false,
     *       "pipelineExecutionId" : null,
     *       "disabledJobs" : [ ]
     *     },
     *     "scheduleId" : "3",
     *     "firstSchedTime" : "2017-03-31 11:45:21"
     *   }
     * }
     */
    public String fetchSchecule(String projectName,String flowId) {
        if(null == sessionId) {
            login();
        }

        String s = fetchProjectFlows(projectName);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String projectId = jsonObject.getString("projectId");
        String res = restTemplate
                .getForObject(uri + "/schedule?ajax=fetchSchedule&session.id={1}&&projectId={2}&flowId={3}"
                        , String.class, sessionId,projectId,flowId
                );
        logger.info("azkban schedule info:{}", res);
        return res;
    }

    /**
     * 移除定时任务
     * @param scheduleId
     * @return
     */
    public String removeSched(String scheduleId) {
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("action", "removeSched");
        linkedMultiValueMap.add("scheduleId", scheduleId);
        String res = restTemplate.postForObject(uri + "/schedule",linkedMultiValueMap,String.class);
        return res;
    }

    /**
     * 给定一个执行ID，此API调用获取该执行的所有详细信息，包括所有作业执行的列表。
     * @param execid 执行id
     * @return
     */
    public String fetchexecflow(String execid) {
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/executor?ajax=fetchexecflow&session.id={1}&execid={2}"
                        , String.class, sessionId,execid
                );
        return res;
    }

    /**
     * 调用相应作业的日志
     * @param execid 执行id
     * @param jobId 作业id
     * @param offset 偏移量
     * @param length 日志长度
     * @return
     */
    public String fetchExecJobLogs(String execid,String jobId,Integer offset,Integer length) {
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/executor?ajax=fetchExecJobLogs&session.id={1}&execid={2}&jobId={3}&offset={4}&length={5}"
                        , String.class, sessionId,execid,jobId,offset,length);
        return res;
    }

    /**
     * 根据lastUpdateTime筛查流
     * @param execid
     * @return {@See https://azkaban.github.io/azkaban/docs/2.5/#api-execute-a-flow}
     *
     */
    public String fetchexecflowupdate(String execid) {
        return fetchexecflowupdate(execid,"-1");
    }

    /**
     * 根据lastUpdateTime筛查流
     * @param execid
     * @param lastUpdateTime
     * @return {@See https://azkaban.github.io/azkaban/docs/2.5/#api-execute-a-flow}
     *
     */
    public String fetchexecflowupdate(String execid, String lastUpdateTime) {
        if (null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "/executor?ajax=fetchExecJobLogs&session.id={1}&execid={2}&lastUpdateTime={3}"
                        , String.class, sessionId,execid,lastUpdateTime);
        return res;
    }

    /**
     * 获取项目日志信息
     * @param projectName 项目名
     * @return
     */
    public String fetchProjectLogs(String projectName) {
        if(null == sessionId) {
            login();
        }
        String res = restTemplate.getForObject(uri +"/manager?ajax=fetchProjectLogs&session.id={1}&project={2}",
                String.class,sessionId,projectName);
        return res;
    }

    private static void disableChecks() {
        try {
            SSLUtils.trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum PeriodEnum{
        MONTH("M"),WEEK("w"),DAYS("d"),HOURS("h"),MINUTES("m"),SECONDS("s");
        private String value;

        PeriodEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



}
