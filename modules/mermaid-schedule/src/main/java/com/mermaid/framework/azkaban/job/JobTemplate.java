package com.mermaid.framework.azkaban.job;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;

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
            String path = JobTemplate.class.getClassLoader().getResource("resources").getPath();
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
}
