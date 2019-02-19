package com.mermaid.netty.protocol;

import com.mermaid.netty.protocol.support.MarshallingCodecFactory;
import org.jboss.marshalling.Marshaller;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/1/14 22:58
 * version 1.0
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    private Marshaller marshaller;

    public MarshallingEncoder() {
        marshaller = (Marshaller) MarshallingCodecFactory.buildMarshalling();
    }
}
