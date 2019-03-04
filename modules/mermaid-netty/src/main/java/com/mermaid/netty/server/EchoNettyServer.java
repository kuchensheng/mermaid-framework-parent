package com.mermaid.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

import java.net.InetSocketAddress;

/**
 * Desription:
 * 创建一个ServerBootStrap的实例以引导和绑定服务器
 * 创建并分配一个NIOEventLoopGroup实例以进行事件的处理，如接受新连接以及读/写数据
 * 指定服务器绑定的本地InetSocketAddress
 * 使用一个EchoServerHandler的实例初始化每一个新的Channel
 * 调用ServerBootStrap.bind()方法以绑定服务器
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
                    //ChannelInitializer的作用，当一个新的连接被接受时，将会创建一个新的Channel。
                    //而ChannelInitializer将会把你的EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                    //这个ChannelHandler将会受到有关入站消息的通知
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("msg decoder", new MessageToByteEncoder<String>() {
                                @Override
                                protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {

                                }
                            });
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
