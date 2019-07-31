package com.mermaid.framework.azkaban.job;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.util.*;

/**
 * ClassName:JobTemplate
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  15:48
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 15:48   kuchensheng    1.0
 */
public class JobTemplate {

    private static Template getTemplate() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
        try {
            String path = JobTemplate.class.getClassLoader().getResource("template").getPath();
            cfg.setDirectoryForTemplateLoading(new File(path));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
            Template template = cfg.getTemplate("template.ftl");
            return template;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建command
     * @param jobName 作业名称，在Azkaban上展示的作业名
     * @param commands 作业的执行命令
     * @param DCS_JOB_ID DCS的JOBID，为查询作业执行状态的参数
     * @param dependencies 依赖的上级job
     * @return
     */
    public static String createCommand(String jobName,String[] commands,String DCS_JOB_ID,String[] dependencies) {

        String res = null;
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(stringWriter);
        Map<String,Object> root = new HashMap<>();
        root.put("jobName",jobName);
        root.put("type","command");
        if(null == commands) {
            return null;
        }
        List<String> commandList = new ArrayList<>();
        for (String cs : commands) {
            commandList.add(cs);
            commandList.add("sh /home/test/data/common.sh " + DCS_JOB_ID);
        }
        root.put("commands", commandList);
        if(null != dependencies) {
            String strDepen = "";
            for (String str : dependencies) {
                strDepen = strDepen + str + ",";
            }
            if(strDepen.endsWith(",")) {
                strDepen = strDepen.substring(0,strDepen.length() - 1);
            }
            root.put("dependencies",strDepen);
        }

        try {
            getTemplate().process(root,bw);
            bw.flush();
            res = stringWriter.getBuffer().toString();
            bw.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 创建zip文件
     * @param map <JobName,Content>
     * @return
     */
    public static File createZipFile(String flowName,Map<String,String> map) {
        String path = JobTemplate.class.getClassLoader().getResource("jobs").getPath();
        try {
            ZipArchiveOutputStream zous = new ZipArchiveOutputStream(new File(path + "/"+flowName+".zip"));
            zous.setUseZip64(Zip64Mode.AsNeeded);
            for (Map.Entry<String,String> entry :map.entrySet()) {
                byte[] bytes = entry.getValue().getBytes();
                ArchiveEntry entry1 = new ZipArchiveEntry(entry.getKey() +".job");
                zous.putArchiveEntry(entry1);
                zous.write(bytes);
                zous.closeArchiveEntry();

            }
            zous.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(path +"/"+flowName+".zip");
    }




    public static void main(String[] args) {
        String test = JobTemplate.createCommand("test", new String[]{"echo 666"},"15", new String[]{"1","2"});
        System.out.println(test);

        Map<String,String> map = new HashMap<>();
        map.put("job1",test);
        File text = JobTemplate.createZipFile("text", map);
    }
}
