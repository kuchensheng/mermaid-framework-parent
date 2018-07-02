package com.mermaid.framework.websocket.netty;

import com.mermaid.framework.websocket.declaration.WebSocketServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/6/24 21:24
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class.getName());
    private static final String WEBSOCKET = "websocket";
    private static final String UPGRADE="Upgrade";

    private WebSocketServerHandshaker handshaker;

    private int port;
    private String strUri;

    public WebSocketServerHandler(String strUri,int port) {
        this.strUri = strUri;
        this.port = port;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统HTTP接入
        if(msg instanceof FullHttpRequest) {
            logger.info("Full HttpRequest ,not a websocket,{}",msg);
            handleHttpRequest(ctx,(FullHttpRequest) msg);
        }else if(msg instanceof WebSocketFrame) {
            logger.info("Reqest is Websocket Request");
            handleWebSocketFrame(ctx,(WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的命令
        if(frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),(CloseWebSocketFrame) frame.retain());
            return;
        }

        //做判断是否是Ping消息
        if(frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        //暂时支持文本消息
        if(!(frame instanceof TextWebSocketFrame )) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }
        //返回应答消息
        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
        String request = textWebSocketFrame.text();

        logger.info("received : {}",request);

        ctx.channel().write(new TextWebSocketFrame(request+",欢迎使用Netty Websocket服务，现在时刻："+new java.util.Date().toString()));
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        //如果Http解码失败，返回HTTP异常
        if(!request.decoderResult().isSuccess() || (!WEBSOCKET.equals(request.headers().get(UPGRADE)))) {
            logger.error("FullHttpRequest decoder Failure");
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应返回
        String strUrl = "ws://localhost:".concat(String.valueOf(port).concat(strUri.startsWith("/") ? strUri :"/"+strUri));
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(strUrl,null,false);
        handshaker = webSocketServerHandshakerFactory.newHandshaker(request);
        if(null == handshaker) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else
            handshaker.handshake(ctx.channel(),request);
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        //返回应答给客户端
        if(response.status().code() != HttpStatus.OK.value()) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
        }

        //如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if(!isKeepAlive(request) || response.status().code() != HttpStatus.OK.value()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private boolean isKeepAlive(FullHttpRequest request) {
        return !request.headers().get("Connection").toString().equals("close");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
