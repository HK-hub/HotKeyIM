package com.hk.im.server.chat.server.handler.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author : HK意境
 * @ClassName : ChannelActiveStateHandler
 * @date : 2023/1/4 20:05
 * @description : 用户处理客户端上下线的 handler
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ChannelActiveStateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        super.exceptionCaught(ctx, cause);
    }
}

