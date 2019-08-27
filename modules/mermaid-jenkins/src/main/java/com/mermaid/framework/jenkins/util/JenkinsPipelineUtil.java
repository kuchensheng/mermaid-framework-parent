package com.mermaid.framework.jenkins.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JenkinsPipelineUtil {
    private static final Logger logger = LoggerFactory.getLogger(JenkinsPipelineUtil.class);

    private static final String TEMP_DIR="src/main/resources/static/template";

    enum TemplatePathEnum {
        MAVEN("pipeline_template_maven_deploy.ftl"),
        /**
         * 配置信息模板
         */
        CONFIG("jenkins_config_template.ftl"),

        CONFIG_VIEW("jenkins_view_template.ftl"),
        ;
        private String value;

        TemplatePathEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static Template getTemplate(String tempPath) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        try {
            cfg.setDirectoryForTemplateLoading(new File(TEMP_DIR));
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
            Template template = cfg.getTemplate(tempPath);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Template getTemplate(TemplatePathEnum templatePathEnum) {
        return getTemplate(templatePathEnum.getValue());
    }

    public static String createMavenJobConfigXml(Map<String, Object> rootMap) {
        String script = fullTemplate(rootMap,TemplatePathEnum.MAVEN);
        rootMap.put("script",script);
        String configXml = fullTemplate(rootMap,TemplatePathEnum.CONFIG);
        //转义
        configXml = configXml.replaceAll("&","&amp;");
        org.dom4j.Document document = null;
        if((document =  parseStringToXml(configXml)) != null) {
            configXml = document.asXML();
        }
        return configXml;

    }

    private static Document parseStringToXml(String configXml) {
        try {
            return new SAXReader().read(new ByteArrayInputStream(configXml.getBytes()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 模板填充
     * @param map 参数
     * @param templatePathEnum 模板类型
     * @return
     */
    public static String fullTemplate(Map<String,Object> map,TemplatePathEnum templatePathEnum) {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(stringWriter);
        try {
            getTemplate(templatePathEnum).process(map,bw);
            bw.flush();
            String s = stringWriter.getBuffer().toString();
            bw.close();
            return s;
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void main(String[] args) {
        Map<String,Object> root = new HashMap<>();
        root.put("pipeline","pipeline");

        List<Map<String,String>> tools = new ArrayList<>();
        Map<String,String> tool = new HashMap<>(1);
        tool.put("toolsName","jdk");
        tool.put("toolsVersion","1.7.149");
        tools.add(tool);
        tool = null;
        tool = new HashMap<>(1);
        tool.put("toolsName","maven");
        tool.put("toolsVersion","apache-maven-3.5.0");
        tools.add(tool);

        root.put("tools",tools);

        String gitUrl = "XXX.git";
        String credentialsId = "XXXXXXXXXXXXXX";
        String branch = "XXXXX";

        root.put("credentialsId",credentialsId);
        root.put("gitUrl",gitUrl);
        root.put("branch",branch);
        root.put("settingsUrl","http://666666666.xml");
        root.put("jobName","test0001");

        root.put("callbackUrl","http://callback");

        root.put("repositoryId","");
        root.put("repositoryUrl","");
        root.put("cmdParameters","");


        StringWriter stringWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(stringWriter);
        try {
            getTemplate(TemplatePathEnum.MAVEN).process(root,bw);
            bw.flush();
            String s = stringWriter.getBuffer().toString();
            bw.close();
            System.out.println(s);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
