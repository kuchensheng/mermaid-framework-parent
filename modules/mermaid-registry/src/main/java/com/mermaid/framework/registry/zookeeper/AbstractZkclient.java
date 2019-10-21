package com.mermaid.framework.registry.zookeeper;

import com.mermaid.framework.registry.AbstractRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/8 11:12
 */
public abstract class AbstractZkclient extends AbstractRegistry implements IZookeeperClient {

    private Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);

    @Override
    public void delete(String path) {
        List<String> children = getChildren(path);
        if(null != children || children.size() > 0) {
            logger.info("path = ->{}有子节点，删除其子节点信息");
            for (String child :children) {
                delete(child);
            }
        }else {
            doDelete(path);
        }
    }

    protected abstract void doDelete(String path);
}
