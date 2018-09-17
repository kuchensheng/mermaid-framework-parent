package com.mermaid.framework.mvc.error;

import com.mermaid.framework.mvc.APIResponse;
import com.mermaid.framework.mvc.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 16:01
 * version 1.0
 */
@ControllerAdvice
public class MermaidExceptionHandler {

    private static Properties i18nProperites = new Properties();
    private static final String I18N_CONFIG="classpath*:i18n/api*.properties";
    private static final Logger logger = LoggerFactory.getLogger(MermaidExceptionHandler.class);
    static {
        try {
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(I18N_CONFIG);
            if(null != resources && resources.length > 0) {
                for (Resource resource : resources) {
                    String absoluteFile = resource.getURL().getPath();
                    logger.info("读取i18n文件路径："+absoluteFile);
                    Properties moduleResources = null;
                    if(("jar").equals(resource.getURL().getProtocol())){
                        absoluteFile = absoluteFile.substring(absoluteFile.lastIndexOf("!")+1);
                        resource = new ClassPathResource(absoluteFile);
                    }
                    i18nProperites.putAll(PropertiesLoaderUtils.loadProperties(resource));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public APIResponse handle(Exception ex) {
        if(ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            APIResponse apiResponse = businessException.response();
            String[] errorMessageArguments = businessException.getErrorMessageArguments();
            String code = businessException.getErrorCode();
            if(null != i18nProperites && i18nProperites.size() > 0 && i18nProperites.containsKey(code)) {
                String errMsg = i18nProperites.getProperty(code);
                apiResponse.setMessage(errMsg);
            }else {
                StringBuilder stringBuilder = new StringBuilder();
                if(null != errorMessageArguments && errorMessageArguments.length > 0) {
                    for (String errMsg : errorMessageArguments) {
                        stringBuilder.append(errMsg);
                    }
                }
                apiResponse.setMessage(stringBuilder.toString());
            }
            return apiResponse;
        } else {
            return APIResponse.fail(ex);
        }
    }
}
