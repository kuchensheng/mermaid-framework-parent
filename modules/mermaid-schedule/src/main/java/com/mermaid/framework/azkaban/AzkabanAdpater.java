package com.mermaid.framework.azkaban;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mermaid.framework.util.SSLUtils;
import com.mermaid.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/26 20:58
 * version 1.0
 */
public class AzkabanAdpater {
    private static final Logger logger = LoggerFactory.getLogger(AzkabanAdpater.class);

    private String uri;

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

    /**
     * 登陆azkaban
     * @throws Exception
     */
    private void login(){
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/azkaban.properties");
            properties.load(resourceAsStream);
            login(properties.getProperty("mermaid.azkaban.uri"),properties.getProperty("mermaid.azkaban.username"),properties.getProperty("mermaid.azkaban.password"));
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
    private void login(String uri,String userName,String password) throws Exception {
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
    }

    /**
     * 创建项目
     * @param projectName 项目名称
     * @return 创建结果 json字符串
     */
    private Boolean createProjects(String projectName) {
        return createProjects(projectName,projectName);
    }

    /**
     * 创建项目
     * @param projectName 项目名
     * @param description 描述
     * @return 创建结果 json字符串
     */
    private Boolean createProjects(String projectName, String description) {
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
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(uri + "/manager", httpEntity, JSONObject.class);
        if(HttpStatus.OK.value() == jsonObjectResponseEntity.getStatusCode().value()) {
            JSONObject body = jsonObjectResponseEntity.getBody();
            logger.info("azkaban create project info :{}",body.toJSONString());
            return "success".equals(body.getString("status"));
        }
        logger.info("azkaban create project failure:{}",JSONObject.toJSONString(jsonObjectResponseEntity));
        return false;
    }

    /**
     * 删除项目
     * @param projectName 项目名
     * @return 删除结果
     */
    private boolean deleteProject(String projectName) {
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

    /**
     * 文件上传
     * @param zipFilePath zip文件地址
     * @param projectName 项目名
     * @return 返回操作结果 json串
     * {
    "error" : "Installation Failed.\nError unzipping file.",
    "projectId" : "192",
    "version" : "1"
    }
     */
    public String uploadZip(String zipFilePath, String projectName) {
        if(null == sessionId) {
            login();
        }
        FileSystemResource resource = new FileSystemResource(new File(zipFilePath));
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "upload");
        linkedMultiValueMap.add("project", projectName);
        linkedMultiValueMap.add("file", resource);
        String res = restTemplate.postForObject(uri + "/manager", linkedMultiValueMap, String.class);
        logger.info("azkaban upload zip:{}", res);
        return res;
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
     * 获取流的作业
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
     * 获取溜的执行列表
     * @param projectName
     * @param flowId
     * @param start
     * @param length
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
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "fetchFlowExecutions");
        linkedMultiValueMap.add("project", projectName);
        linkedMultiValueMap.add("flow", flowId);
        linkedMultiValueMap.add("start",null == start ? 0 : start);
        if(null != length || 0 != length) {
            linkedMultiValueMap.add("length",length);
        }

        String res = restTemplate.getForObject(uri + "/manager",String.class,linkedMultiValueMap);

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
     * @param projectName 项目名
     * @param execId exceid
     * @return
     */
    public boolean cancleExecutorFlow(String projectName,String execId) {
        String res = restTemplate
                .getForObject(uri + " /executor?ajax=cancelFlow&session.id={1}&execid={2}"
                        , String.class, sessionId,execId
                );
        logger.info("azkban executions that are currently running:{}", res);
        return res.contains("error");
    }

    /**
     * 定时执行任务
     * @param projectName
     * @param flowName
     * @param scheduleTime
     * @param scheduleDate
     * @param period
     * @param periodEnum
     * @return
     */
    public String scheduleFlow(String projectName,String flowName,String scheduleTime,String scheduleDate, Integer period,PeriodEnum periodEnum) {
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "scheduleFlow");
        linkedMultiValueMap.add("projectName", projectName);
        linkedMultiValueMap.add("flowName", flowName);
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

        String res = restTemplate.getForObject("/schedule",String.class,linkedMultiValueMap);
        return res;
    }

    public String scheduleFlow(String projectName,String flowName,String cron) {
        if(null == sessionId) {
            login();
        }
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "scheduleFlow");
        linkedMultiValueMap.add("projectName", projectName);
        linkedMultiValueMap.add("flow", flowName);
        linkedMultiValueMap.add("cronExpression",cron);

        return restTemplate.getForObject("/schedule",String.class,linkedMultiValueMap);
    }

    /**
     *
     * @param projectId
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
    public String fetchSchecule(String projectId,String flowId) {
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "ajax=fetchSchedule&session.id={1}&&projectId={2}&flowId={3}"
                        , String.class, sessionId,projectId,flowId
                );
        logger.info("azkban schedule info:{}", res);
        return res;
    }

    public String removeSched(String scheduleId) {
        if(null == sessionId) {
            login();
        }
        String res = restTemplate
                .getForObject(uri + "action=removeSched&session.id={1}&&scheduleId={2}"
                        , String.class, sessionId,scheduleId
                );
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

    public static void main(String[] args) throws Exception {
        AzkabanAdpater adpater = new AzkabanAdpater();
        String projectFlows = adpater.fetchProjectFlows("abc");
        System.out.println(projectFlows);
        JSONObject jsonObject = JSONObject.parseObject(projectFlows);
        JSONArray flows = jsonObject.getJSONArray("flows");
        String flowId = ((JSONObject) flows.get(0)).getString("flowId");
        System.out.println("flowId = " + flowId);

//        String fetchJobsFlow = adpater.fetchJobsFlow(jsonObject.getString("project"),flowId);
//        System.out.println(fetchJobsFlow);

        String executorFlow = adpater.executorFlow(jsonObject.getString("project"), flowId);
        System.out.println(executorFlow);

        String fetchexecflow = adpater.fetchexecflow(JSONObject.parseObject(executorFlow).getString("execid"));
        System.out.println(fetchexecflow);
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
