package com.mermaid.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/10/21 23:27
 * version 1.0
 */
public class EchoNettyServer {
    private final int port;

    public EchoNettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println(
                    "Usage:"+EchoNettyServer.class.getSimpleName() +
                            "<port>"
            );

        }
        int port = Integer.parseInt(args[0]);
        new EchoNettyServer(port).start();
    }

    private void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
