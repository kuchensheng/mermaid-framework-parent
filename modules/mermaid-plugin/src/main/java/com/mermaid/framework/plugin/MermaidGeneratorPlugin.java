package com.mermaid.framework.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/28 9:32
 */
public class MermaidGeneratorPlugin extends PluginAdapter {

    private static final String XML_FILE_POSTFIX = "Extension";
    private static final String JAVA_FILE_POSTFIX = "Extension";
    private static final String BASIC_PACKAGE = "basic";
    private static final String EXTENSION_PACKAGE = "extension";
    private static String ANNOTATION_RESOURCE = "org.apache.ibatis.annotations.Mapper";
    private static final String MAPPER_EXT_INFORMATION = "<!-- 扩展自定义的SQl语句写在此文件中 -->";
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(transferExtensionName(introspectedTable.getMyBatis3JavaMapperType(), "Mapper", "Extension").replace("basic", "extension"));
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        context.getCommentGenerator().addJavaFileComment(interfaze);
        FullyQualifiedJavaType baseInterfaze = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        interfaze.addSuperInterface(baseInterfaze);
        FullyQualifiedJavaType annotation = new FullyQualifiedJavaType(ANNOTATION_RESOURCE);
        interfaze.addAnnotation("@Mapper");
        interfaze.addImportedType(annotation);
        interfaze.addImportedType(baseInterfaze);
        CompilationUnit compilationUnits = interfaze;
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(compilationUnits, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty("javaFileEncoding"), context.getJavaFormatter());
        if (isExistExtFile(generatedJavaFile.getTargetProject(), generatedJavaFile.getTargetPackage(), generatedJavaFile.getFileName()))
        {
            return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        } else
        {
            List generatedJavaFiles = new ArrayList(1);
            generatedJavaFiles.add(generatedJavaFile);
            return generatedJavaFiles;
        }
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        String extensionFileName = transferExtensionName(introspectedTable.getMyBatis3XmlMapperFileName(), "Mapper.xml", "Extension").replace("basic", "extension");
        if (isExistExtFile(context.getSqlMapGeneratorConfiguration().getTargetProject(), introspectedTable.getMyBatis3XmlMapperPackage().replace("basic", "extension"), extensionFileName))
        {
            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        } else
        {
            Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
            XmlElement root = new XmlElement("mapper");
            document.setRootElement(root);
            String namespace = transferExtensionName(introspectedTable.getMyBatis3SqlMapNamespace(), "Mapper", "Extension").replace("basic", "extension");
            root.addAttribute(new Attribute("namespace", namespace));
            root.addElement(new TextElement("<!-- 扩展自定义的SQl语句写在此文件中 -->"));
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, extensionFileName, introspectedTable.getMyBatis3XmlMapperPackage().replace("basic", "extension"), context.getSqlMapGeneratorConfiguration().getTargetProject(), false, context.getXmlFormatter());
            List answer = new ArrayList(1);
            answer.add(gxf);
            return answer;
        }
    }

    private boolean isExistExtFile(String targetProject, String targetPackage, String fileName)
    {
        File project = new File(targetProject);
        if (!project.isDirectory()){
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (StringTokenizer st = new StringTokenizer(targetPackage, "."); st.hasMoreTokens(); sb.append(File.separatorChar)) {
            sb.append(st.nextToken());
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory())
        {
            boolean rc = directory.mkdirs();
            if (!rc) {
                return true;
            }
        }
        File testFile = new File(directory, fileName);
        return testFile.exists();
    }

    private static String transferExtensionName(String origin, String anchor, String swap)
    {
        String extensionName = "";
        int chatAt;
        if (origin != null && !"".equals(origin.trim()) && (chatAt = origin.indexOf(anchor)) > 0){
            extensionName = (new StringBuilder()).append(origin.substring(0, chatAt)).append(swap).append(origin.substring(chatAt)).toString();
        }
        return extensionName;
    }
}
