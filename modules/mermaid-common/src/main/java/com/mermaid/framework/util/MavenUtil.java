package com.mermaid.framework.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;

/**
 * ClassName:MavenUtil
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  09:14
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 09:14   kuchensheng    1.0
 */
public class MavenUtil {

    private static final Logger logger = LoggerFactory.getLogger(MavenUtil.class);

    private static final String DEFAULT_POM_PATH = System.getProperty("user.dir").concat(File.separator).concat("pom.xml");

    private static Model getModel() throws Exception{
       return getModel(DEFAULT_POM_PATH);
    }

    private static Model getModel(String pomPath) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(pomPath));
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model read = reader.read(fileInputStream);
        return read;
    }

    /**
     * 获取标签内容
     * @param tagName 标签内容
     * @return
     * @throws Exception
     */
    public static String getTagContent(String tagName) throws Exception {
        return getTagContent(DEFAULT_POM_PATH,tagName);
    }

    public static String getTagContent(String pomPath,String tagName) throws Exception {
        Model model = getModel(pomPath);
        Field declaredField = Model.class.getDeclaredField(tagName);
        declaredField.setAccessible(true);
        Object o = declaredField.get(model);
        return String.valueOf(o);
    }
}
