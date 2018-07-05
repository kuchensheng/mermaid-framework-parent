package com.mermaid.framework.jenkins.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/5 23:01
 */
public class JenkinPipelineUtil {
    private static final Logger logger = LoggerFactory.getLogger(JenkinPipelineUtil.class);

    private static final String TEMP_DIR="F:\\javaproj\\mermaid-framework-parent\\mermaid-jenkins\\src\\main\\resources\\static\\template";

    enum TemplatePathEnum {
        MAVEN("pipeline_template_maven.ftl")
        ,
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
