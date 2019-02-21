package com.mermaid.framework.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/21 22:43
 * version 1.0
 */
public class HessianSeializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        if(null == obj) {
            throw new NullPointerException();
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T desrialize(byte[] data, Class<T> clazz) {
        if(null == data) {
            throw new NullPointerException();
        }
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            return (T) hessianInput.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
