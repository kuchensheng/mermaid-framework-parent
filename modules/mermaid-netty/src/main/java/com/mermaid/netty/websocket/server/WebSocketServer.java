package com.mermaid.netty.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/1/13 22:24
 * version 1.0
 */
public class WebSocketServer {

    public static void main(String[] args) {
        int port = 8080;
        if(args.length >0) {
            port = Integer.parseInt(args[0]);
        }
        new WebSocketServer().run(port);
    }

    private void run(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            serverBootstrap.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加HttpServerCodec，将请求和应答消息编码或者解码为Http消息
                            pipeline.addLast("http-codec",new HttpServerCodec());
                            //添加HttpObjectAggregator，将HTTP消息的多个部分组合成一条完成的HTTP消息
                            pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                            //添加ChunkedWriteHandler，来向客户端发送HTML5文件，主要用于支持浏览器和服务端进行WebSocket通信
                            pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                            pipeline.addLast("handler",new WebsocketServerHandler());
                        }
                    });
            Channel ch = serverBootstrap.bind(port).sync().channel();
            System.out.println("Web socket server started at port " + port +".");
            System.out.println("Open your browser and naviate to http://localhost:"+port+"/");
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }

    }
}
