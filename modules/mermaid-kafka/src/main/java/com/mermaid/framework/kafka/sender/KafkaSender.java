package com.mermaid.framework.kafka.sender;

import com.mermaid.framework.kafka.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg("");
    }

}
