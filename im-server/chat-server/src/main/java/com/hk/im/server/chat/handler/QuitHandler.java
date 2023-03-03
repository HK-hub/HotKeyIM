package com.hk.im.server.chat.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : QuitHandler
 * @date : 2022/12/28 21:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    // 连接建立
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{} 建立连接", ctx.channel());
        super.channelActive(ctx);
    }

    // 连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经离线", ctx.channel());
        super.channelInactive(ctx);
    }


    // 捕捉到异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经异常断开，异常：{}", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }
}
