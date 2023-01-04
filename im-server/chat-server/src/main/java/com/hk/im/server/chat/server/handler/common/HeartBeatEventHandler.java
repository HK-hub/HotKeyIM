package com.hk.im.server.chat.server.handler.common;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : HeartBeatEventHandler
 * @date : 2023/1/4 16:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class HeartBeatEventHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 用来触发特殊事件
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            log.info("已经30分钟 没有读取到数据了");
            // 处理释放，断开链接
            this.doReadIdleStateEvent(ctx);
        }
        super.userEventTriggered(ctx, evt);
    }


    /**
     * 处理读超时事件
     * @param ctx
     */
    private void doReadIdleStateEvent(ChannelHandlerContext ctx) {
        // 释放连接
        ctx.channel().close();
    }

}
