package com.mermaid.framework.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/16 21:38
 * version 1.0
 */
public class KafkaConsumer {
    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "118.31.175.223:19093");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "duanjt_test");
        p.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
        p.put("security.protocol", SecurityProtocol.SASL_PLAINTEXT.name);
        p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"06lR@E\";");
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(p);
        kafkaConsumer.subscribe(Collections.singletonList("mermaid-core-info"));// 订阅消息

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format("topic:%s,offset:%d,消息:%s", //
                        record.topic(), record.offset(), record.value()));
                kafkaConsumer.commitAsync();
            }
        }
    }
}
