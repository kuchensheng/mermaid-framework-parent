package com.mermaid.framework.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @Desription:JSON序列化工具
 * @Author:Hui
 * @CreateDate:2018/8/19 13:50
 */
@Slf4j
public class JSONSerializer implements ISerializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DATE_TIME_MODULE = "DateTimeModule";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String NULL = "null";
    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS,Boolean.TRUE);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,Boolean.TRUE);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,Boolean.TRUE);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,Boolean.TRUE);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,Boolean.FALSE);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,Boolean.FALSE);
        SimpleModule module = new SimpleModule(DATE_TIME_MODULE, Version.unknownVersion());
        module.addSerializer(Date.class,new JSONSerializer.FDateSerializer());
        module.addDeserializer(Date.class,new JSONSerializer.FDateDeserializer());
        OBJECT_MAPPER.registerModule(module);
    }

    private static ObjectMapper getObjectMapperInstance() {
        return OBJECT_MAPPER;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        if(null == obj) {
            return new byte[0];
        }

        try {
            String json = OBJECT_MAPPER.writeValueAsString(obj);
            return json.getBytes();
        } catch (JsonProcessingException e) {
            log.error("json serializer error {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String json = new String(data);
        try {
            return (T) OBJECT_MAPPER.readValue(json,clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class FDateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            jsonGenerator.writeString(null != date ? format.format(date) : NULL);
        }
    }

    public static class FDateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String date = jsonParser.getText();
            if(StringUtils.isEmpty(date)) {
                return null;
            }
            if(org.apache.commons.lang.StringUtils.isNumeric(date)) {
                return new Date(Long.valueOf(date));
            }

            try {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                return format.parse(date);
            } catch (ParseException e) {
                log.error("date deserialize"+e);
                throw new IOException(e);
            }
        }
    }
}
