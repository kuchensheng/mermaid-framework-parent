package com.mermaid.framework.registry.zookeeper;

import com.mermaid.framework.serialize.ISerializer;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/22 11:11
 */
public class ZkClientZookeeperClient extends AbstractZkclient {
    private Logger logger = LoggerFactory.getLogger(ZkClientZookeeperClient.class);

    private final ZKClientWrapper client;

    private volatile Watcher.Event.KeeperState state = Watcher.Event.KeeperState.SyncConnected;

    public ZkClientZookeeperClient(String servers,int sessionTimeout,int connectTimeout,ISerializer serializer) {
        client = new ZKClientWrapper(servers,sessionTimeout,connectTimeout);
        client.addStateListener(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                ZkClientZookeeperClient.this.state = keeperState;
                if(state == Watcher.Event.KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if(state == Watcher.Event.KeeperState.SyncConnected) {
                    stateChanged(StateListener.CONNECTED);
                }
            }

            @Override
            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
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
        return false;
    }

    @Override
    protected void createPersistent(String path, Object data) {
        client.createPersistent(path,data);
    }

    @Override
    protected void createEphemeral(String path, Object data) {
        client.createEphemeral(path,data);
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
    public void delete(String path) {
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
                listener.doChildChange(s,list);
            }
        });
    }

    @Override
    protected List<String> doAddChildListener(String path, IChildListener childListener) {
        return client.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                childListener.doChildChange(s,list);
            }
        });
    }
}
