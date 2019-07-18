package com.mermaid.framework.kafka.sender;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/16 21:35
 * version 1.0
 */
public class KafkaSender {
    public static void main(String[] args) throws InterruptedException {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "118.31.175.223:19093");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
        p.put("security.protocol",SecurityProtocol.SASL_PLAINTEXT.name);
        p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"06lR@E\";");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(p);

        try {
            while (true) {
                String msg = "Hello," + new Random().nextInt(100) + LocalDateTime.now().toString();
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("info.gateway-biz", msg);
                kafkaProducer.send(record);
                System.out.println(kafkaProducer.toString());
                System.out.println("消息发送成功:" + msg);
                Thread.sleep(2000);
            }
        } finally {
            kafkaProducer.close();
        }

    }
}
