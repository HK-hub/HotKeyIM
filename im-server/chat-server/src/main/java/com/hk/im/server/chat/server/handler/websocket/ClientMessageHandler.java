package com.hk.im.server.chat.server.handler.websocket;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import com.hk.im.domain.message.control.ConnectMessage;
import com.hk.im.domain.message.system.NoSupportMessage;
import com.hk.im.server.chat.server.channel.UserChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

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
public class ClientMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于管理和记录客户端的 channel
	private static ChannelGroup clientChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 建立连接
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("链接创建：{}", ctx.channel().remoteAddress());
		clientChannels.add(ctx.channel());
		super.channelActive(ctx);
	}

	/**
	 * 断开连接
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("链接断开：{},id={}", ctx.channel().remoteAddress(), ctx.channel().id().asLongText());
		clientChannels.remove(ctx.channel());
		super.channelInactive(ctx);
	}

	/**
	 * 发送异常处理
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 异常处理
		cause.printStackTrace();
		// 关闭连接，移除channel
		ctx.channel().close();
		clientChannels.remove(ctx.channel());

		super.exceptionCaught(ctx, cause);
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
		// 数据校验
		WebSocketMessage message = null;
		MessageConstants.MessageActionType actionType = null;
		try {
			message = JSON.parseObject(data, WebSocketMessage.class);
			actionType = MessageConstants.MessageActionType.values()[message.getMessageActionType()];
		} catch (Exception e) {
			// 数据校验错误：格式错误，消息类型不支持
			ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(new NoSupportMessage())));
		}

		// 消息转发
		this.dispatch(ctx,msg, message, actionType);
		// 日志记录
	}


	/**
	 * 做消息的分发处理
	 * @param ctx
	 * @param msg
	 * @param message
	 */
	private void dispatch(ChannelHandlerContext ctx, TextWebSocketFrame msg, WebSocketMessage message, MessageConstants.MessageActionType actionType) {

		// 消息转发
		if (MessageConstants.MessageActionType.CONNECT.equals(actionType)) {
			// 连接请求：
			ConnectMessage connectMessage = (ConnectMessage) message.getMessageData();
			log.info("用户:{}, 请求连接Chat Server", connectMessage);
			// 添加 channel 到 manager
			UserChannelManager.add(connectMessage.getUserId(), ctx.channel());
			// 发布事件或消息

		}
		// 业务层处理数据
		// 响应客户端
	}




}