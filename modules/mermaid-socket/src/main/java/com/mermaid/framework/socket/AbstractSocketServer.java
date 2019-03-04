package com.mermaid.framework.socket;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/1 10:59
 */
public abstract class AbstractSocketServer implements SocketServer{
    
    private static final InternalLogger logger = Log4JLoggerFactory.getInstance(AbstractSocketServer.class);
    
    private int port;
    
    protected String name = "untitled";
    
    protected boolean running;

    protected EventLoopGroup bossGroup;

    protected EventLoopGroup workGroup;

    protected ChannelFuture channelFuture;

    protected SocketChannel socketChannel;

    @Override
    public void start(int port) {
        synchronized (this) {
            if(this.isRunning()) {
                logger.info("服务已启动，请勿重复启动");
                return;
            }
            setPort(port);
            setRunning(Boolean.TRUE);
            initialize();
            doStart(port);
        }
    }

    @Override
    public void stop() {
        synchronized (this) {
            if(!this.isRunning()) {
                logger.info("服务未启动!!");
                return;
            }
            setRunning(Boolean.FALSE);
            doStop();
        }
    }

    protected void initialize() {
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
    }


    protected abstract void doStop();

    protected abstract void doStart(int port);
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
