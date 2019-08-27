package com.mermaid.framework.jenkins.module;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/27 22:39
 * version 1.0
 */
public enum EnumJobType {
    SNAPSHOTS("0"),RELEASE("1");

    private String value;

    EnumJobType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
