package com.mermaid.framework.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/28 9:24
 */
public class MybatisCommentExtensionGenerator extends DefaultCommentGenerator {
    public MybatisCommentExtensionGenerator() {
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if(null != introspectedColumn.getRemarks() && !introspectedColumn.getRemarks().equals("")) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine((new StringBuilder()).append(" * ").append(introspectedColumn.getRemarks()).toString());
            field.addJavaDocLine(" */");
        }
    }
}
