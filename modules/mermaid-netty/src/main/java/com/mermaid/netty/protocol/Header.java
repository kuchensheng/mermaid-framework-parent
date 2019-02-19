package com.mermaid.netty.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Desription:消息头Header类定义
 *
 * @author:Hui CreateDate:2019/1/14 22:35
 * version 1.0
 */
public final class Header {
    private int crcCode = 0xabef0101;
    //消息长度
    private int lenght;
    //会话ID
    private long sessionID;
    //消息类型
    private byte type;
    //消息优先级
    private byte priority;
    //附件
    private Map<String,Object> attachement = new HashMap<>();

    public final int getCrcCode() {
        return crcCode;
    }

    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public final int getLenght() {
        return lenght;
    }

    public final void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public final long getSessionID() {
        return sessionID;
    }

    public final void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final byte getPriority() {
        return priority;
    }

    public final void setPriority(byte priority) {
        this.priority = priority;
    }

    public final Map<String, Object> getAttachement() {
        return attachement;
    }

    public final void setAttachement(Map<String, Object> attachement) {
        this.attachement = attachement;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", lenght=" + lenght +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachement=" + attachement +
                '}';
    }
}
