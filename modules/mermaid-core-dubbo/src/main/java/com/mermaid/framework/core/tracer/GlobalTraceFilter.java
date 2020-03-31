package com.mermaid.framework.core.tracer;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.config.factory.GlobalRuntimeConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * ClassName:GlobalTraceFilter
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:10
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:10   kuchensheng    1.0
 */
@Activate(group = {Constants.CONSUMER,Constants.PROVIDER},order = Integer.MIN_VALUE)
public class GlobalTraceFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(GlobalTraceFilter.class);
    private static final String TRACE_ID = "traceId";
    private static final String SPANID = "spanId";
    private static final String PARENT_SPAN = "parentId";
    private static final String TOPIC = "trace_topic";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS");

    private static KafkaProducer<String,String> kafkaProducer = null;
    static {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GlobalRuntimeConfigFactory.getInstance().getValue("spring.kafka.bootstrap-servers","localhost:9092"));
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        p.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.mermaid.framework.kafka.sender.TestPartitioner");
//        p.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
//        p.put("security.protocol", SecurityProtocol.SASL_PLAINTEXT.name);
//        p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"06lR@E\";");


       kafkaProducer = new KafkaProducer<>(p);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = invocation.getAttachment(TRACE_ID);
        String spanId = invocation.getAttachment(SPANID);
        LocalDateTime dateTime = LocalDateTime.now();
        traceId = StringUtils.hasText(traceId) ? traceId : UUID.randomUUID().toString();
        String serviceId = GlobalRuntimeConfigFactory.getInstance().getValue("spring.application.name");
        if (!StringUtils.isEmpty(traceId)) {
            RpcContext.getContext().setAttachment("traceId", UUID.randomUUID().toString());
        } else {
            RpcContext.getContext().setAttachment("traceId",traceId);
        }

        if (StringUtils.hasText(spanId)) {
            RpcContext.getContext().setAttachment(PARENT_SPAN,spanId);
        } else {
            RpcContext.getContext().set(SPANID,serviceId);
        }
        try {
            return invoker.invoke(invocation);
        } catch (RpcException e) {
            throw e;
        } finally {
            LocalDateTime now = LocalDateTime.now();
            long costTime = now.getNano() - dateTime.getNano();
            Span span = new Span(traceId,serviceId,spanId,dateTime.format(formatter),now.format(formatter),costTime,invocation.getMethodName(),invocation.getArguments());
            senderToKafka(span);
        }

    }

    private void senderToKafka(Span span) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(GlobalRuntimeConfigFactory.getInstance().getValue("mermaid.trace.log.topic",TOPIC), JSONUtils.toJSONString(span));
            kafkaProducer.send(record);
            kafkaProducer.close();
        } catch (Exception e) {
            logger.warn("trace log error，please check kafka properites",e);
        }
    }

    public class Span {
        private String traceId;
        private String spanId;
        private String parentSpanId;
        private String rpcTime;
        private String endTime;
        private Long costTime;
        private String methodName;
        private Object[] arguments;

        public Span(String traceId, String spanId, String parentSpanId, String rpcTime, String endTime, Long costTime, String methodName) {
            this.traceId = traceId;
            this.spanId = spanId;
            this.parentSpanId = parentSpanId;
            this.rpcTime = rpcTime;
            this.endTime = endTime;
            this.costTime = costTime;
            this.methodName = methodName;
        }

        public Span(String traceId, String spanId, String parentSpanId, String rpcTime, String endTime, Long costTime, String methodName, Object[] arguments) {
            this.traceId = traceId;
            this.spanId = spanId;
            this.parentSpanId = parentSpanId;
            this.rpcTime = rpcTime;
            this.endTime = endTime;
            this.costTime = costTime;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getSpanId() {
            return spanId;
        }

        public void setSpanId(String spanId) {
            this.spanId = spanId;
        }

        public String getParentSpanId() {
            return parentSpanId;
        }

        public void setParentSpanId(String parentSpanId) {
            this.parentSpanId = parentSpanId;
        }

        public String getRpcTime() {
            return rpcTime;
        }

        public void setRpcTime(String rpcTime) {
            this.rpcTime = rpcTime;
        }

        public Long getCostTime() {
            return costTime;
        }

        public void setCostTime(Long costTime) {
            this.costTime = costTime;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public void setArguments(Object[] arguments) {
            this.arguments = arguments;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
