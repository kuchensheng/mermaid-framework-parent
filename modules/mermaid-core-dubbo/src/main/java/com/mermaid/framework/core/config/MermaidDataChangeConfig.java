package com.mermaid.framework.core.config;

import com.mermaid.framework.core.apollo.MermaidDataChangeListener;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.config.factory.GlobalRuntimeConfigFactory;
import com.mermaid.framework.registry.zookeeper.ZKClientWrapper;
import org.I0Itec.zkclient.IZkDataListener;
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

        zkClientWrapper.subscribeDataChanges("/applications/"+applicationInfo.getAppName()+"/data", (IZkDataListener) new MermaidDataChangeListener(configurableListableBeanFactory));
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
                zkClientWrapper.createPersistent(childPath);
            }
        }
        zkClientWrapper.createEphemeral(path,applicationInfo.getRuntimeProperties().getProperties());
    }
}
