package com.mermaid.framework.core.config;

import com.mermaid.framework.core.apollo.MermaidDataChangeListener;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.config.factory.GlobalRuntimeConfigFactory;
import com.mermaid.framework.registry.zookeeper.ZKClientWrapper;
import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/19 23:05
 * version 1.0
 */
@Configuration
@ConditionalOnExpression("${mermaid.framework.cloud.enable:false} == true ")
public class MermaidDataChangeConfig implements ApplicationRunner{

    private static final Logger logger = LoggerFactory.getLogger(MermaidDataChangeConfig.class);

    @Autowired
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    ApplicationInfo applicationInfo = ApplicationInfo.getInstance();

    @Bean
    public ZKClientWrapper zkClientWrapper() {

        String zkAddress = GlobalRuntimeConfigFactory.getInstance().getProperties().getProperty("dubbo.registry.address");
        if(!StringUtils.isEmpty(zkAddress) && zkAddress.startsWith("zookeeper://")) {
            zkAddress = zkAddress.substring(12);
        }
        ZKClientWrapper zkClientWrapper = new ZKClientWrapper(zkAddress);
        return zkClientWrapper;
    }

    @Autowired
    private ZKClientWrapper zkClientWrapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String znode = "/applications/"+applicationInfo.getAppName()+"/data";
        if(zkClientWrapper.exists(znode)) {
            logger.info("订阅数据变化，监听zk节点：{}",znode);
            zkClientWrapper.subscribeDataChanges(znode, new MermaidDataChangeListener(configurableListableBeanFactory));
        }
        String host = applicationInfo.getAppHost();
        host = StringUtils.replace(host,".","_");
        //TODO 级联创建znode
        String path = "/applications/"+applicationInfo.getAppName()+"/Instances/"+host+"_"+applicationInfo.getAppPort();
        String[] childPaths = path.substring(1).split("/");
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<childPaths.length -1;i++) {
            sb.append("/").append(childPaths[i]);
            String childPath = sb.toString();
            if(!zkClientWrapper.exists(childPath)) {
                logger.info("创建持久节点{}",childPath);
                zkClientWrapper.createPersistent(childPath);
            }
        }
        logger.info("创建临时节点，znode={},并将实例配置信息写入该节点",path);
        zkClientWrapper.createEphemeral(path,applicationInfo.getRuntimeProperties().getProperties());
    }
}
