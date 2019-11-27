package com.mermaid.framework.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/16 21:38
 * version 1.0
 */
public class KafkaConsumer {
    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "kucs_1111");
        p.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"1024");
        p.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        p.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
//        p.put("security.protocol", SecurityProtocol.SASL_PLAINTEXT.name);
//        p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"06lR@E\";");
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(p);
        kafkaConsumer.subscribe(Collections.singletonList("commander.prod"));// 订阅消息

        int amount = 0;
        Map<String,Integer> repeatData = new HashMap<>();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(20));
            System.out.println("recores length="+records.count());
            if (records.count() == 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format("%s", //
                        record.value()));
//                JSONObject jsonObject = JSONObject.parseObject(record.value());
//                write2File(jsonObject.getString("key"), jsonObject.getBytes("img_data"));
                kafkaConsumer.commitAsync();
//                System.out.println("amount="+ ++ amount);
            }
        }
    }

    private static void write2File(String key ,byte[] data) {
//        byte[] data = img_data.getBytes();
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File("/Users/kuchensheng/Desktop/test_consumer/"+key+".jpg"));
            imageOutput.write(data,0,data.length);
            imageOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
