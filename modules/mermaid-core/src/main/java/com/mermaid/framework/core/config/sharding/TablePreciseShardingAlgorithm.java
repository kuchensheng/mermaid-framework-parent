package com.mermaid.framework.core.config.sharding;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * Desription:分表算法
 *
 * @author:Hui CreateDate:2019/4/26 0:17
 * version 1.0
 */
public class TablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, final PreciseShardingValue<Long> preciseShardingValue) {
        int size = collection.size();
        for (String each : collection) {
            if(each.endsWith(preciseShardingValue.getValue() % size + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
