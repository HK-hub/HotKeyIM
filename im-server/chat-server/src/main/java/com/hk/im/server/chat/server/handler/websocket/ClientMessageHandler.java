package com.hk.im.server.chat.server.handler.websocket;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import com.hk.im.server.chat.server.handler.dispatch.HandlerDispatcher;
import com.hk.im.server.common.channel.UserChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : ClientMessageHandler
 * @date : 2023/1/4 18:52
 * @description : 处理监听 Text 文本事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 消息分发
        HandlerDispatcher.dispatch(ctx,msg);
        super.channelRead(ctx,msg);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

    }


    /**
     * 校验数据类型和action
     *
     * @param msg
     *
     * @return
     */
    private Boolean doCheck(TextWebSocketFrame msg) {

        String data = msg.text();
        // 数据校验
        WebSocketMessage message = null;
        MessageConstants.MessageActionType actionType = null;
        try {
            // 校验数据类型和 action
            message = JSON.parseObject(data, WebSocketMessage.class);
            actionType = MessageConstants.MessageActionType.values()[message.getMessageActionType()];
            return true;
        } catch (Exception e) {
            // 数据校验错误：格式错误，消息类型不支持
            return false;
        }
    }


    /**
     * 建立连接
     *
     * @param ctx
     *
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("链接创建：{}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    /**
     * 断开连接
     *
     * @param ctx
     *
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("链接断开：{},id={}", ctx.channel().remoteAddress(), ctx.channel().id().asLongText());
        UserChannelManager.remove(ctx.channel());
        super.channelInactive(ctx);
    }

    /**
     * 发送异常处理
     *
     * @param ctx
     * @param cause
     *
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常处理
        cause.printStackTrace();
        // 关闭连接，移除channel
        ctx.channel().close();
        UserChannelManager.remove(ctx.channel());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeCompletedEvent = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = handshakeCompletedEvent.requestUri(); // 握手请求 URI
            HttpHeaders headers = handshakeCompletedEvent.requestHeaders(); // 握手请求头
            log.info("握手请求：URI={}，headers={}", uri, headers.entries());
        }
    }

}