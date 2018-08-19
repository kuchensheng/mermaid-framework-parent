package com.mermaid.framework.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



public class NettyWebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketServer.class);

    private Integer port;
    private String strUri;
    private static final String HTTP_CODEC="httpcodec";
    private static final String HTTP_OBJECT_AGGREGATOR="aggregator";
    private static final String HTTP_CHUNKED="http-chunked";
    private static final String WEBSOCKET_HANDLER="handler";

    public NettyWebSocketServer(String strUri,Integer port) {
        this.port = port;
        this.strUri = strUri;
        run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(HTTP_CODEC,new HttpServerCodec())
                                    .addLast(HTTP_OBJECT_AGGREGATOR,new HttpObjectAggregator(65536))
                                    .addLast(HTTP_CHUNKED,new ChunkedWriteHandler())
                                    .addLast(WEBSOCKET_HANDLER,new WebSocketServerHandler(strUri,port));
                        }
                    });
            Channel channel = serverBootstrap.bind(port).sync().channel();
            logger.info("(Netty) Web socket server started at port {}",port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("(Netty) Web socket server started failer,\n{}",e);
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
