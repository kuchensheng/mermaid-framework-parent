package com.mermaid.framework.jenkins.util;

import com.mermaid.framework.jenkins.module.JobServerRelation;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/25 22:37
 * version 1.0
 */
public class XmlUtil {

    private static SAXTransformerFactory sf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

    public static void writeXml(JobServerRelation jobServerRelation) {
        OutputStream in =null;

        try {
            TransformerHandler transformerHandler = sf.newTransformerHandler();
            //2、通过SAXTransformerFactory创建一个TransformerHandler对象
            TransformerHandler handler = sf.newTransformerHandler();
            //3、通过TransformerHandler对象获取Transformer对象(用于设置xml输出的样式和头）
            Transformer transformer = handler.getTransformer();
            //设置没有其他的DTD(Document Type Defination 文档类型定义）规范
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            //设置编码格式，显式的显示在<?xml version="1.0" ?>中
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            //设置换行
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //文件输出
            File file =new File("/Users/kuchensheng/mermaid-framework-parent/modules/mermaid-jenkins/src/main/resources/Job_server_config.xml");
            //确保file是存在的
            if(!file.exists()){
                if(!file.createNewFile()){
                    throw new FileNotFoundException("文件创建失败！");
                }
            }
            //4、创建输出流OutputStream对象
            in = new FileOutputStream(file);
            //5、创建流Result对象
            Result result = new StreamResult(in);
            //6、关联result，此时有了生成元素的方法（handler提供的方法）和装元素的容器（result对象）
            handler.setResult(result);
            //打开文档
            handler.startDocument();
            //属性设置
            AttributesImpl attr = new AttributesImpl();
            //开始创建元素
            handler.startElement("", "", "JobServerRelation", attr);
            attr.addAttribute("", "", "JobName", "", jobServerRelation.getJobName());
            attr.addAttribute("", "", "buildNumber", "", jobServerRelation.getBuildNumber()+"");
            attr.clear();
            attr.addAttribute("", "", "Name", "", "阿德拉尔");
            attr.addAttribute("", "", "Code", "", "ADR");
            handler.startElement("", "", "JenkinsServer", attr);
            handler.endElement("", "", "State");
            handler.endElement("", "", "CountryRegion");
            //结束元素创建
            handler.endElement("", "", "Location");
            //关闭文档
            handler.endDocument();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        writeXml(null);
    }

}
