package com.mermaid.framework.im.handler;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/2/14 17:22
 */
@ChannelHandler.Sharable
public class ImServerHandler extends ChannelHandlerAdapter{

    private final static Logger logger = LoggerFactory.getLogger(ImServerHandler.class);

    public ImServerHandler() {
        super();
    }

    @Override
    protected void ensureNotSharable() {
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        return super.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
