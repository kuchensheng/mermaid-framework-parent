package com.mermaid.framework.registry;

import com.mermaid.framework.registry.zookeeper.AbstractZkclient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/8 11:09
 */
public abstract class AbstractRegistry implements Registry{
    private static final Logger logger = LoggerFactory.getLogger(AbstractZkclient.class);

    private final Set<IStateListener> stateListeners = new CopyOnWriteArraySet<>();

    private volatile boolean closed = false;

    private static final String SEPARATOR = "/";

    protected void stateChanged(int state) {
        for (IStateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    private Set<IStateListener> getSessionListeners() {
        return this.stateListeners;
    }

    @Override
    public void create(String path, boolean ephemeral) {
        if(!ephemeral) {
            if(checkExists(path)) {
                return;
            }
        }
        int index = path.indexOf(SEPARATOR);
        if(index > -1) {
            create(path.substring(0,index),false);
        }
        if(ephemeral) {
            createEphemeral(path);
        }else {
            createPersistent(path);
        }
    }

    @Override
    public void create(String path, boolean ephemeral,Object data) {
        if(ephemeral && checkExists(path)) {
            delete(path);
        }
        String[] childs = path.split(SEPARATOR);
        StringBuilder stringBuilder = new StringBuilder(SEPARATOR);
        for (int i = 1; i< childs.length;i++) {
            stringBuilder.append(childs[i]);
            if(checkExists(stringBuilder.toString())) {
                stringBuilder.append(SEPARATOR);
                continue;
            }

            if (i != childs.length - 1) {
                if ((stringBuilder.lastIndexOf(SEPARATOR) <= -1)) {
                    stringBuilder.append(SEPARATOR);
                }
                createPersistent(stringBuilder.toString());
            } else {
                createEphemeral(stringBuilder.toString());
            }
        }
        setData(stringBuilder.toString(),data);
    }


    @Override
    public void addStateListener(IStateListener listener) {
        stateListeners.add(listener);
    }

    @Override
    public void removeStateListener(IStateListener listener) {
        stateListeners.remove(listener);
    }

    @Override
    public void close() {
        if(closed) {
            return;
        }
        closed = true;
        try {
            doClose();
        } catch (Throwable throwable) {
            logger.warn(throwable.getMessage(),throwable);
        }
    }

    @Override
    public boolean isConnected() {
        return !closed;
    }

    @Override
    public List<?> lookup(String serviceNamePath) {
        List<String> children = getChildren(serviceNamePath);
        if(null == children || children.size() == 0) {
            logger.info("节点path -> {},无子节点信息，请检查",serviceNamePath);
            return null;
        }
        List<Object> result = new ArrayList<>();
        for (String child : children) {
            Object data = getData(child);
            if(null != data) {
                result.add(data);
            }

        }
        return result;
    }

    protected abstract void doClose();

    protected abstract boolean checkExists(String path);

    protected abstract void setData(String path, Object data);


    protected abstract void createEphemeral(String path);

    protected abstract void createPersistent(String path);

}
