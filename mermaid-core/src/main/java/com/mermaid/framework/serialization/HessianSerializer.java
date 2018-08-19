package com.mermaid.framework.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * version 1.0
 * Desription: Hession序列化
 * 支持跨语言传输的二进制序列化协议
 * @author:Hui
 * CreateDate:2018/8/19 14:14
 */
@Slf4j
public class HessianSerializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        if(null == obj) {
            throw new NullPointerException("obj is null");
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            log.error("hession serializer error",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if(null == data) {
            throw new NullPointerException();
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(is);
            return (T) hi.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
