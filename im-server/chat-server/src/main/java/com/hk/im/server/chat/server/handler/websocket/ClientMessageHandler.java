package com.hk.im.server.chat.server.handler.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.message.WebSocketMessage;
import com.hk.im.domain.message.control.ConnectMessage;
import com.hk.im.domain.message.system.NoSupportMessage;
import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.chat.server.channel.UserChannelManager;
import com.hk.im.server.chat.util.SpringUtils;
import com.hk.im.service.service.AuthorizationService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        this.dispatch(ctx, msg);
        super.channelRead(ctx, msg);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

    }

    /**
     * 连接处理
     * BUG: 这里request.uri() 是请求路径上得参数，参数被编码了，需要自己转码
     *
     * @param ctx
     * @param msg
     */
    private void doConnect(ChannelHandlerContext ctx, Object msg) {
        // 第一次的连接请求，请求升级为Websocket
        FullHttpRequest request = (FullHttpRequest) msg;
        if (Objects.isNull(request)) {
            ctx.close();
        }
        String uri = request.uri();
        log.info("request:uri={},headers={},method={}", uri, request.headers().get("Origin"), request.method());
        if (StringUtils.isEmpty(request.headers().get("Origin"))) {
            log.info("request origin is empty");
            ctx.close();
        }

        // 处理请求参数
        String path = null;
        String token = null;
        if (StringUtils.contains(uri, ":")) {
            // Map<String, String> urlParams = getUrlParams(uri);
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
            try{
                String[] split = uri.split(":");
                path = split[0];
                token = split[1];
                log.info("request url params:token={}", token);
                request.setUri(path);
            }catch(Exception e){
                e.printStackTrace();
                ctx.close();
            }
        }

        // 验证 usi
        log.info("the FullHttpRequest new uri={}", request.uri());
        if (!Objects.equals(request.uri(), MetaDataConfig.path)) {
            // 请求路径错误: 关闭连接
            log.info("the websocket path is error: expect {}, but provide {}", request.uri(), MetaDataConfig.path);
            ctx.channel().close();
        }

        // 认证处理
        AuthorizationService authorizationService = SpringUtils.getBean(AuthorizationService.class);
        User user = authorizationService.authUserByToken(token);
        if (Objects.isNull(user)) {
            // 认证失败
            log.info("this websocket owner is not authorized", ctx.channel().id().asLongText());
            ctx.channel().close();
        }

        // 添加 channel
        UserChannelManager.add(user.getId(), ctx.channel());
        // 认证成功，发布事件消息

    }


    /**
     * 做消息的分发处理
     *
     * @param ctx
     * @param msg
     */
    private void dispatch(ChannelHandlerContext ctx, Object msg) {
        // 连接建立请求
        if (msg instanceof FullHttpRequest) {
            this.doConnect(ctx, msg);
        } else if (msg instanceof TextWebSocketFrame) {
            // 业务消息
            this.process(ctx, msg);
        }
    }


    /**
     * 处理业务消息
     * @param ctx
     * @param msg
     */
    private void process(ChannelHandlerContext ctx, Object msg) {

    }


    private void dispatch(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

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
     * 获取 http 请求，上的uri 参数
     *
     * @param url
     *
     * @return
     */
    private static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            return map;
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