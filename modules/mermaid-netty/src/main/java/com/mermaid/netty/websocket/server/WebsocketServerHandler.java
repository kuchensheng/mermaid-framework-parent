package com.mermaid.netty.websocket.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/1/13 22:29
 * version 1.0
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = Logger.getLogger(WebsocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;
    private ChannelHandlerContext ctx;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        //传统HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest((FullHttpRequest) msg);
        }
        //Websocket接入
        else if(msg instanceof WebSocketFrame) {
            handleWebSocketFrame((WebSocketFrame) msg);
        }
    }

    private void handleWebSocketFrame(WebSocketFrame frame) {
        //判断是否是关闭链路的命令
        if(frame instanceof CloseWebSocketFrame) {
            this.handshaker.close(this.ctx.channel(),(CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            this.ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
         //本例仅支持文本消息，不支持二进制消息
        if(!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }
        //返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        if(logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s",this.ctx.channel(),request));
        }
        this.ctx.channel().write(new TextWebSocketFrame(request +",欢迎使用Netty WebSocket服务，现在时刻："+new Date().toString()));
    }

    private void handleHttpRequest(FullHttpRequest req) {
        //如果HTTP解码失败，返回HTTP异常
        if(!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        this.handshaker = wsFactory.newHandshaker(req);
        if (this.handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(this.ctx.channel());
        }else {
            handshaker.handshake(this.ctx.channel(),req);
        }
    }

    private void sendHttpResponse(FullHttpRequest req, FullHttpResponse response) {
        //返回应答给客户
        if(response.getStatus()!= HttpResponseStatus.OK) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(response,response.content().readableBytes());
        }

        //如果是非Keep-Alive，关闭连接
        ChannelFuture future = this.ctx.writeAndFlush(response);
        if(!isKeepAlive(req) || response.getStatus().code() != HttpResponseStatus.OK.code()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private boolean isKeepAlive(FullHttpRequest request) {
        return !request.headers().get("Connection").toString().equals("close");
    }
}
