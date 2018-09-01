package com.mermaid.framework.util;

import java.util.UUID;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/31 20:55
 * version 1.0
 */
public class StringUtil {

    public static String parse2UUID(String origin) {
        return UUID.nameUUIDFromBytes(origin.getBytes()).toString();
    }
}
