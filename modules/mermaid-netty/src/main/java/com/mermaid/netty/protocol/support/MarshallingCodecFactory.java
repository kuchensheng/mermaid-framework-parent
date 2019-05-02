package com.mermaid.netty.protocol.support;

import com.mermaid.netty.protocol.MarshallingEncoder;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import javax.xml.bind.Marshaller;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/1/14 23:02
 * version 1.0
 */
public class MarshallingCodecFactory {
    static final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
    static final MarshallingConfiguration configuration = new MarshallingConfiguration();
    static final Integer VERSION = 5;
    public static io.netty.handler.codec.marshalling.MarshallingEncoder buildMarshalling() {
        configuration.setVersion(VERSION);
        MarshallerProvider provider = new DefaultMarshallerProvider(factory,configuration);
        io.netty.handler.codec.marshalling.MarshallingEncoder encoder = new io.netty.handler.codec.marshalling.MarshallingEncoder(provider);
        return encoder;
    }


}
