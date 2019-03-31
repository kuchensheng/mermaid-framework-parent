package com.mermaid.framework.registry;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/31 22:42
 * version 1.0
 */
public interface IDataListener {

    void dataChanged(String path, Object value, EventType eventType);
}
