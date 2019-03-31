package com.mermaid.framework.registry.zookeeper;

import com.mermaid.framework.registry.EventType;
import com.mermaid.framework.registry.IChildListener;
import com.mermaid.framework.registry.IDataListener;
import com.mermaid.framework.registry.IStateListener;
import com.mermaid.framework.serialize.ISerializer;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/22 11:11
 */
public class ZookeeperClient extends AbstractZkclient {
    private Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private final ZKClientWrapper client;

    private volatile Watcher.Event.KeeperState state = Watcher.Event.KeeperState.SyncConnected;

    public ZookeeperClient(String servers, int sessionTimeout, int connectTimeout) {
        client = new ZKClientWrapper(servers, sessionTimeout, connectTimeout);
        client.addStateListener(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                ZookeeperClient.this.state = keeperState;
                if (state == Watcher.Event.KeeperState.Disconnected) {
                    stateChanged(IStateListener.DISCONNECTED);
                } else if (state == Watcher.Event.KeeperState.SyncConnected) {
                    stateChanged(IStateListener.CONNECTED);
                }
            }

            @Override
            public void handleNewSession() throws Exception {
                stateChanged(IStateListener.RECONNECTED);
            }
        });
        client.start();
    }

    @Override
    protected void doClose() {
        client.close();
    }

    @Override
    protected boolean checkExists(String path) {
        return this.client.exists(path);
    }

    @Override
    protected void createPersistent(String path, Object data) {
        client.createPersistent(path, data);
    }

    @Override
    protected void createEphemeral(String path, Object data) {
        client.createEphemeral(path, data);
    }

    @Override
    protected void createEphemeral(String path) {
        client.createEphemeral(path);
    }

    @Override
    protected void createPersistent(String path) {
        client.createPersistent(path);
    }

    @Override
    protected void doDelete(String path) {
        client.delete(path);
    }

    @Override
    public List<String> getChildren(String path) {
        return client.getChildren(path);
    }

    @Override
    public void removeChildListener(String path, IChildListener listener) {
        client.unsubscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                listener.doChildChange(s, list);
            }
        });
    }

    @Override
    public <T> T getData(String path) {
        return client.getData(path);
    }

    @Override
    public void addChildListener(String path, IChildListener childListener) {
        client.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                childListener.doChildChange(s,list);
            }
        });
    }

    @Override
    public void addDataListener(String path, IDataListener dataListener) {
        client.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                dataListener.dataChanged(dataPath,data, EventType.NodeDataChanged);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                dataListener.dataChanged(dataPath,null,EventType.NodeDataDeleted);
            }
        });
    }


}
