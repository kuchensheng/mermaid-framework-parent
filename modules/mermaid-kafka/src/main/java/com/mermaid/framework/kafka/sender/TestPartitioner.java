package com.mermaid.framework.kafka.sender;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * ClassName:TestPartitioner
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  17:30
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 17:30   kuchensheng    1.0
 */
public class TestPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        try {
            final String strVal = new String(valueBytes);
            final int int_Val = Integer.parseInt(strVal);
            return int_Val;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
