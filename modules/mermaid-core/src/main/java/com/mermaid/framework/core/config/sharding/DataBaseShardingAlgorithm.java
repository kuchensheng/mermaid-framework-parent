package com.mermaid.framework.core.config.sharding;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;
import java.util.concurrent.locks.Condition;

/**
 * Desription:分库策略
 *
 * @author:Hui CreateDate:2019/4/26 0:10
 * version 1.0
 */
public class DataBaseShardingAlgorithm implements PreciseShardingAlgorithm<Integer>{
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        int size = collection.size();
        for (String each : collection) {
            if(each.endsWith(preciseShardingValue.getValue() % size + "")) {
                return each;
            }
        }

        throw new UnsupportedOperationException();
    }
}
