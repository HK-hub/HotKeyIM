package com.hk.im.server.chat.server.handler.dispatch;


import com.hk.im.server.chat.server.handler.custom.WebSocketConnectHandler;
import com.hk.im.server.chat.server.handler.custom.WebSocketMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;


/**
 * @author : HK意境
 * @ClassName : HandlerDispatcher
 * @date : 2023/2/24 20:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class HandlerDispatcher {

    /**
     * 做消息的分发处理
     *
     * @param ctx
     * @param msg
     */
    public static void dispatch(ChannelHandlerContext ctx, Object msg) {
        // 连接建立请求
        if (msg instanceof FullHttpRequest) {
           // doConnect(ctx, msg);
            WebSocketConnectHandler.doConnect(ctx, msg);
        } else if (msg instanceof TextWebSocketFrame) {
            // 业务消息
            WebSocketMessageHandler.process(ctx, msg);
        }
    }

}
