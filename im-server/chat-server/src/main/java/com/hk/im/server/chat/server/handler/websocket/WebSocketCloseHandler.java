package com.hk.im.server.chat.server.handler.websocket;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @ClassName : ClientMessageHandler
 * @author : HK意境
 * @date : 2023/1/4 18:52
 * @description : 处理监听 Text 文本事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class WebSocketCloseHandler extends SimpleChannelInboundHandler<CloseWebSocketFrame> {

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("链接断开：{}", ctx.channel().remoteAddress());
		super.channelInactive(ctx);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("链接创建：{}", ctx.channel().remoteAddress());
		super.channelActive(ctx);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CloseWebSocketFrame msg) throws Exception {
		String data = msg.reasonText();
		// 业务层处理数据
		log.info("客户端主动关闭：{}", data);
		//this.discardService.discard(textWebSocketFrame.text());
		// 执行关闭业务

	}
}