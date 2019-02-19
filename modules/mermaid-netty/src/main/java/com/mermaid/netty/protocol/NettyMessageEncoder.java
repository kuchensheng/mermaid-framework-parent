package com.mermaid.netty.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;

import java.io.IOException;
import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/1/14 22:44
 * version 1.0
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    private MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() {
        this.marshallingEncoder = new MarshallingEncoder();
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {

    }
}
