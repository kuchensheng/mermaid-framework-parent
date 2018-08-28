package com.mermaid.framework.plugin;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/28 10:06
 */
public class IntrospectedTableMyBatis3ExtensionImpl extends IntrospectedTableMyBatis3Impl {
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List generatedXmlFiles = new ArrayList();
        if (xmlMapperGenerator != null)
        {
            Document document = xmlMapperGenerator.getDocument();
            boolean mergeable = false;
            if ("true".equalsIgnoreCase(context.getProperty("mergeable"))) {
                mergeable = true;
            }
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(), context.getSqlMapGeneratorConfiguration().getTargetProject(), mergeable, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                generatedXmlFiles.add(gxf);
            }
        }
        return generatedXmlFiles;
    }
}
