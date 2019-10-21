package com.mermaid.framework.kafka.sender;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/7/16 21:35
 * version 1.0
 */
public class KafkaSender {
    private static final Integer[] codes={9515,0,9600,5};
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka4:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        p.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.mermaid.framework.kafka.sender.TestPartitioner");
        p.put(SaslConfigs.SASL_MECHANISM,"PLAIN");
        p.put("security.protocol", SecurityProtocol.SASL_PLAINTEXT.name);
        p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"06lR@E\";");


        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(p);
        KafkaSender sender = new KafkaSender();
        while (true) {
            AtomicInteger id = new AtomicInteger(1);
            try {
                while (id.get() <= 10000) {
                    try {
                        String[] uuids = createUUIDs();
                        String msg = sender.createCallLog(id.getAndIncrement(),uuids);
                        ProducerRecord<String, String> record = new ProducerRecord<String, String>("gateway_apiCallLog_pig", msg);
                        kafkaProducer.send(record,(recordMetadata,e) -> {
                            if( e != null) {
                                e.printStackTrace();
                            } else {
                                System.out.println("消息发送成功:"+msg);
                                System.out.println("所在分区："+recordMetadata.partition());
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                TimeUnit.SECONDS.sleep(2);
            }
        }

    }

    private static  String[] createUUIDs() {
        String[] uuids = new String[5];
        for (int i=0;i<5;i++) {
            uuids[i] = UUID.randomUUID().toString();
        }
        return uuids;
    }
    public String createCallLog(int id,String[] uuids) throws Exception {
        ApiCallLog apiCallLog = new ApiCallLog();
        Random random = new Random();
        apiCallLog.setApiName("songxiaocai.ssp.customer.save"+random.nextInt(100));
        apiCallLog.setAppKey("41392722");
        apiCallLog.setCallTime(System.currentTimeMillis());
        apiCallLog.setClientIp("192.168.0.153");
        apiCallLog.setCostTime(random.nextInt(100));
        apiCallLog.setErrorCode(codes[random.nextInt(codes.length-1)]);
        apiCallLog.setId(id);
        apiCallLog.setPlatformCostTime(5060);
        apiCallLog.setRequestSnapshot("这是个打字段");
        apiCallLog.setResponseSnapshot("这个是响应字段");
        apiCallLog.setTraceId(uuids[random.nextInt(uuids.length)]);
        apiCallLog.setCallSnapshot("[1,2,3]");
        return JSONObject.toJSONString(apiCallLog);
    }



    class ApiCallLog implements Serializable {
        private static final long serialVersionUID = -4069296325248132152L;
        private static final String BLANK = "";
        private long id;
        private String apiName = BLANK;
        private String appKey = BLANK;
        private String clientIp = BLANK;
        private String clientVersion = BLANK;
        private String clientSysName = BLANK;
        private String clientSysVersion = BLANK;
        private long userId = 0;
        private String userNick = BLANK;
        private int costTime;
        private int errorCode;
        private String subCode = BLANK;
        private String deviceUUID = BLANK;
        private long callTime = System.currentTimeMillis();
        private int platformCostTime;
        private String requestSnapshot;
        private String responseSnapshot;
        private String callSnapshot;
        private String traceId;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public String getClientVersion() {
            return clientVersion;
        }

        public void setClientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
        }

        public String getClientSysName() {
            return clientSysName;
        }

        public void setClientSysName(String clientSysName) {
            this.clientSysName = clientSysName;
        }

        public String getClientSysVersion() {
            return clientSysVersion;
        }

        public void setClientSysVersion(String clientSysVersion) {
            this.clientSysVersion = clientSysVersion;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserNick() {
            return userNick;
        }

        public void setUserNick(String userNick) {
            this.userNick = userNick;
        }

        public int getCostTime() {
            return costTime;
        }

        public void setCostTime(int costTime) {
            this.costTime = costTime;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getDeviceUUID() {
            return deviceUUID;
        }

        public void setDeviceUUID(String deviceUUID) {
            this.deviceUUID = deviceUUID;
        }

        public long getCallTime() {
            return callTime;
        }

        public void setCallTime(long callTime) {
            this.callTime = callTime;
        }

        public int getPlatformCostTime() {
            return platformCostTime;
        }

        public void setPlatformCostTime(int platformCostTime) {
            this.platformCostTime = platformCostTime;
        }

        public String getRequestSnapshot() {
            return requestSnapshot;
        }

        public void setRequestSnapshot(String requestSnapshot) {
            this.requestSnapshot = requestSnapshot;
        }

        public String getResponseSnapshot() {
            return responseSnapshot;
        }

        public void setResponseSnapshot(String responseSnapshot) {
            this.responseSnapshot = responseSnapshot;
        }

        public String getCallSnapshot() {
            return callSnapshot;
        }

        public void setCallSnapshot(String callSnapshot) {
            this.callSnapshot = callSnapshot;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        @Override
        public String toString() {
            return "ApiCallLog{" +
                    "id=" + id +
                    ", apiName='" + apiName + '\'' +
                    ", appKey='" + appKey + '\'' +
                    ", clientIp='" + clientIp + '\'' +
                    ", clientVersion='" + clientVersion + '\'' +
                    ", clientSysName='" + clientSysName + '\'' +
                    ", clientSysVersion='" + clientSysVersion + '\'' +
                    ", userId=" + userId +
                    ", userNick='" + userNick + '\'' +
                    ", costTime=" + costTime +
                    ", errorCode=" + errorCode +
                    ", subCode='" + subCode + '\'' +
                    ", deviceUUID='" + deviceUUID + '\'' +
                    ", callTime=" + callTime +
                    ", platformCostTime=" + platformCostTime +
                    ", requestSnapshot='" + requestSnapshot + '\'' +
                    ", responseSnapshot='" + responseSnapshot + '\'' +
                    ", callSnapshot='" + callSnapshot + '\'' +
                    ", traceId='" + traceId + '\'' +
                    '}';
        }
    }
}
