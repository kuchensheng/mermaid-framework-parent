package com.mermaid.framework.core.config.sharding;//package com.mermaid.framework.core.config.sharding;
//
//
////import com.google.gson.Gson;
////import com.google.gson.reflect.TypeToken;
//import com.google.common.reflect.TypeToken;
//import io.shardingsphere.core.api.ShardingDataSourceFactory;
//import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
//import io.shardingsphere.core.api.config.strategy.InlineShardingStrategyConfiguration;
//import io.shardingsphere.core.keygen.KeyGenerator;
//
//import io.shardingsphere.core.routing.strategy.ShardingStrategy;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Desription:
// *
// * @author:Hui CreateDate:2019/5/6 22:25
// * version 1.0
// */
//@Configuration
//@ConditionalOnExpression("${mermaid.datasource.sharding.enable:false} == true")
//@Slf4j
//public class DataSourceConfig {
//
//    public static String CLASSPATH_CONFIG_SHARDING_DATASOURCE = "classpath:META-INF/mermaid-framework-sharding.json";
//
//    @Value("classpath:META-INF/mermaid-framework-sharding.json")
//    private Resource resource;
//
//    @Value("${mermaid.datasource.sharding.shardingColumn:user_id}")
//    private String shardingColumn;
//
//    @Value("${mermaid.datasource.sharding.algorithmExpression:db_${user_id % 2}}")
//    private String algorithmExpression;
//
//    @Bean
//    public ShardingStrategy shardingStrategy() {
//        return null;
//    }
//
//    @Bean
//    public DataSource getShardingDataSource() throws Exception{
//        return ShardingDataSourceFactory.createDataSource(dataSourceHashMap(dataSources()),createShardingRuleConfiguration(),new HashMap<String,Object>(),null);
//    }
//
//    private ShardingRuleConfiguration createShardingRuleConfiguration() {
//        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
//        shardingRuleConfiguration.setDefaultKeyGenerator(new KeyGenerator() {
//            @Override
//            public Number generateKey() {
//                return null;
//            }
//        });
//        shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(shardingColumn,algorithmExpression));
//        return shardingRuleConfiguration;
//    }
//
//
//    @Bean
//    public List<Database> dataSources() {
////        try {
////            String databasesString = IOUtils.toString(resource.getInputStream(), Charset.forName("UTF-8"));
////            List<Database> databases = new Gson().fromJson(databasesString, new TypeToken<List<Database>>() {
////            }.getType());
////            return databases;
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        return null;
//    }
//
//    @Bean
//    public Map<String,DataSource> dataSourceHashMap(List<Database> databases) {
//        Map<String,DataSource> dataSourceMap = new HashMap<>();
//        for (Database database : databases) {
//            DataSourceBuilder builder = DataSourceBuilder.create();
//            builder.url(database.getUrl()).driverClassName(database.getDriverClassName())
//                    .username(database.getUsername()).password(database.getPassword());
//            dataSourceMap.put(database.getName(),builder.build());
//        }
//        return dataSourceMap;
//    }
//
//    @Bean
//    public DataSource dataSource() {
////        new ShardingRule()
//        //TODO 创建dataSource sharding
//        return null;
//    }
//
//
//
//    @Data
//    @NoArgsConstructor
//    private class Database {
//        private String name;
//        private String url;
//        private String username;
//        private String password;
//        private String driverClassName;
//
//    }
//
//}
