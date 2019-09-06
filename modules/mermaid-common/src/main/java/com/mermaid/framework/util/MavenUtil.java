package com.mermaid.framework.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/9/6 22:57
 * version 1.0
 */
public class MavenUtil {

    /**
     * 获取项目最外层pom信息
     * @return
     */
    public static Model getMavenPomModel() {
        return getMavenPomModel(System.getProperty("user.dir") + File.separator+"pom.xml");
    }

    public static Model getMavenPomModel(String pomPath) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader(pomPath));
            return model;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

}
