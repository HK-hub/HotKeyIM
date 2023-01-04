package com.hk.im.server.chat.server.handler.websocket;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @ClassName : WebSocketMessageHandler
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
@Component
public class WebSocketMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		log.info("链接断开：{}", ctx.channel().remoteAddress());
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.info("链接创建：{}", ctx.channel().remoteAddress());
	}


	/**
	 * 读取到数据
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		String data = msg.text();
		// 业务层处理数据
		log.info("接收到的数据：{}", data);
		//this.discardService.discard(textWebSocketFrame.text());
		// 响应客户端
		ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端确认消息: " + data + " " + LocalDateTime.now()));
		log.info("响应的数据：{}", data + " " + LocalDateTime.now());

	}



}