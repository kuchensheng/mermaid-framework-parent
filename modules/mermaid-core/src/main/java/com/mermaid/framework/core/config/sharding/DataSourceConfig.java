package com.mermaid.framework.core.config.sharding;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/6 22:25
 * version 1.0
 */
@Configuration
@ConditionalOnExpression("${mermaid.datsource.sharding.enable:false} == true")
@Slf4j
public class DataSourceConfig {

    public static String CLASSPATH_CONFIG_SHARDING_DATASOURCE = "classpath:META-INF/mermaid-framework-sharding.json";

    @Value("classpath:META-INF/mermaid-framework-sharding.json")
    private Resource resource;

    @Bean
    public List<Database> dataSources() {
        try {
            String databasesString = IOUtils.toString(resource.getInputStream(), Charset.forName("UTF-8"));
            List<Database> databases = new Gson().fromJson(databasesString, new TypeToken<List<DataSourceConfig.Database>>() {
            }.getType());
            return databases;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Map<String,DataSource> dataSourceHashMap(List<Database> databases) {
        Map<String,DataSource> dataSourceMap = new HashMap<>();
        for (Database database : databases) {
            DataSourceBuilder builder = DataSourceBuilder.create();
            builder.url(database.getUrl()).driverClassName(database.getDriverClassName())
                    .username(database.getUsername()).password(database.getPassword());
            dataSourceMap.put(database.getName(),builder.build());
        }
        return dataSourceMap;
    }

    @Bean
    public DataSource dataSource() {
//        new ShardingRule()
        //TODO 创建dataSource sharding
        return null;
    }



    @Data
    @NoArgsConstructor
    private class Database {
        private String name;
        private String url;
        private String username;
        private String password;
        private String driverClassName;

    }

}
