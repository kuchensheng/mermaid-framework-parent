package com.mermaid.framework.core.mvc;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


public class ErrorTable {
    private static final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static Properties DEFAULT_ERROR_MESSAGES=new Properties();
    private static Locale DEFAULT_LOCALE=Locale.SIMPLIFIED_CHINESE;
    private final static Map<Locale,Properties> localeMessagesMap = new HashMap<>();
    private static String i18nBaseNames;
    private static boolean initialized = false;

    static {
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.SUCCESS, "API调用成功");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.FAIL, "API调用失败");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.UNAUTHORIZED_SERVICE_INVOKER, "拒绝访问, 未授权的服务调用者");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.VALIDATION_FAIL, "请求参数验证失败");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.BAD_PARAMETER, "拒绝访问, 请求参数错误");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.UNAUTHORIZED, "拒绝访问, 您没有权限请求该资源");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.NOT_INITIALIZED, "返回值未初始化");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.USER_NOT_LOGIN, "用户未登录");
        DEFAULT_ERROR_MESSAGES.setProperty(APIResponse.RPC_FAIL, "远程调用失败【{0}】");
    }

    private static synchronized void init(Locale locale) throws IOException {
        if(!initialized) {
            return ;
        }
        Properties localeMessages = new Properties();
        localeMessagesMap.put(locale, localeMessages);
        String[] resourceNames = i18nBaseNames.split(",");
        for (String resourceName : resourceNames) {
            Resource[] resources = resourcePatternResolver.getResources(resourceName + "*_" + toStandardLocaleString(locale) + ".properties");
            Properties properties = new Properties();
            for (Resource resource : resources) {
                String resourcePath;
                URL path = resource.getURL();
                if (path == null) {
                    resourcePath = resource.getFilename();
                } else {
                    resourcePath = path.getPath();
                }
                InputStreamReader reader = null;
                try {
                    properties.load(new InputStreamReader(resource.getInputStream(), "utf-8"));
                    Set<String> keys = properties.stringPropertyNames();
                    for (String key : keys) {
                        if (localeMessages.containsKey(key)) {
                        }
                        localeMessages.setProperty(key, properties.getProperty(key));
                    }
                }catch (IOException e) {
                    throw e;
                }
                finally {
                    if(reader != null) {
                        try {
                            reader.close();
                        }catch(Exception e) {
                            ; // handled
                        }
                    }
                }
            }
        }
    }

    private static String toStandardLocaleString(Locale locale) {
        return locale.toString();
    }

    public static String convertCode2LocaleMessage(String strCode) {
        Locale locale = LocaleContextHolder.getLocale();
        if(locale == null) {
            locale = DEFAULT_LOCALE;
        }
        return convertCode2LocaleMessage(strCode, locale);
    }

    private static String convertCode2LocaleMessage(Properties localMessages, String strCode) {
        String message = localMessages != null ? localMessages.getProperty(strCode) : null;
        if (message == null) {
            message = DEFAULT_ERROR_MESSAGES.getProperty(strCode);
        }
        if(!StringUtils.hasText(message)) {
            message = strCode;
        }
        return message;
    }

    public static String convertCode2LocaleMessage(String strCode, Locale locale) {
        Properties localeMessages = localeMessagesMap.get(locale);
        if(localeMessages == null) {
            try {
                init(locale);
                localeMessages = localeMessagesMap.get(locale);
            }catch (Exception e) {
            }
        }
        return convertCode2LocaleMessage(localeMessages, strCode);
    }

    public static Properties getErrors() {
        Locale locale = LocaleContextHolder.getLocale();
        if(locale == null) {
            locale = DEFAULT_LOCALE;
        }
        Properties defaultLocaleMessages = localeMessagesMap.get(locale);
        if(defaultLocaleMessages == null) {
            defaultLocaleMessages = new Properties();
        }
        Properties ret = (Properties) defaultLocaleMessages.clone();
        return mergeProperties(ret, DEFAULT_ERROR_MESSAGES);
    }

    private static Properties mergeProperties(Properties src, Properties overwrite) {
        Properties ret = new Properties();
        if (src != null) {
            Enumeration keyEnum = src.keys();
            while (keyEnum.hasMoreElements()) {
                String key = keyEnum.nextElement().toString();
                ret.put(key, src.getProperty(key));
            }
        }

        if (overwrite != null) {
            Enumeration keyEnum = overwrite.keys();
            while (keyEnum.hasMoreElements()) {
                String key = keyEnum.nextElement().toString();
                if (!ret.containsKey(key)) {
                    ret.put(key, overwrite.getProperty(key));
                }
            }
        }

        return ret;
    }
}
