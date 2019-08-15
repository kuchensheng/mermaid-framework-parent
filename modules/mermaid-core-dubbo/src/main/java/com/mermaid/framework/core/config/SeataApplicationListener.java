package com.mermaid.framework.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * ClassName:SeataApplicationListener
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:09
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:09   kuchensheng    1.0
 */
@Component
@ConditionalOnBean(SeataConfig.class)
@Order(1)
public class SeataApplicationListener implements ApplicationRunner, ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationContext applicationContext;
    private String tableName = "undo_log";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("--------------->项目启动完成，初始化seata数据表{}", tableName);
        initSeata();
    }

    private void initSeata() {
        try {
            DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            //从数据库中检测是否存在表名
            ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            if ( rs.next()) {
                logger.info("{}表已存在,无需创建",tableName);
            } else {
                statement.execute(getSql());
                logger.info("{}表创建完成",tableName);
            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            logger.error("",e);
            throw new RuntimeException(tableName + "表创建异常，seata分布式事务不可用");
        }
    }
    private String getSql() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("META-INF/seata.sql");
        InputStream inputStream = classPathResource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        StringBuilder sqlBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sqlBuilder.append(line);
        }
        reader.close();

        return sqlBuilder.toString();
    }
}
