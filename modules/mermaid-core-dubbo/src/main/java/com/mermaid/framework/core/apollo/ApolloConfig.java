package com.mermaid.framework.core.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.config.factory.GlobalRuntimeConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/17 21:14
 * version 1.0
 */
@EnableApolloConfig
@ConditionalOnExpression("${mermaid.cloud.apollo.enable:false} == true")
public class ApolloConfig implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApolloConfig.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ApplicationInfo instance = ApplicationInfo.getInstance();
        String namespace = instance.getAppName()+"_"+instance.getAppHost()+"_"+instance.getAppPort();
        logger.info("监听的namespace 是 {}",namespace);
        Config config = ConfigService.getConfig(namespace);
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    logger.info("key={},old_value={},new_value={}",key,change.getOldValue(),change.getNewValue());
                }

            }
        });
    }
}