package com.mermaid.framework.registry.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/8 11:12
 */
public abstract class AbstractZkclient implements ZookeeperClient{
    private static final Logger logger = LoggerFactory.getLogger(AbstractZkclient.class);

    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<>();

    private volatile boolean closed = false;

    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    private Set<StateListener> getSessionListeners() {
        return this.stateListeners;
    }

    @Override
    public void create(String path, boolean ephemeral) {
        if(!ephemeral) {
            if(checkExists(path)) {
                return;
            }
        }
        int index = path.lastIndexOf('/');
        if(index > 0) {
            create(path.substring(0,index),false);
        }
        if(ephemeral) {
            createEphemeral(path);
        }else {
            createPersistent(path);
        }
    }

    @Override
    public void create(String path, Object data, boolean ephemeral) {
        if(checkExists(path)) {
            delete(path);
        }

        int i = path.lastIndexOf('/');
        if(i > 0) {
            create(path.substring(0,i),false);
        }
        if(ephemeral) {
            createEphemeral(path,data);
        } else {
            createPersistent(path,data);
        }
    }

    @Override
    public List<String> addChildListener(String path, IChildListener childListener) {
        return null;
    }

    @Override
    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    @Override
    public void removeStateListener(StateListener listener) {
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

    protected abstract void doClose();

    protected abstract boolean checkExists(String path);

    protected abstract void createPersistent(String path, Object data);

    protected abstract void createEphemeral(String path, Object data);

    protected abstract void createEphemeral(String path);

    protected abstract void createPersistent(String path);
}
