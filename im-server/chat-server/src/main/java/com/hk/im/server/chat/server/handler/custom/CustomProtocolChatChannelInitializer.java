package com.hk.im.server.chat.server.handler.custom;

import com.hk.im.server.chat.handler.*;
import com.hk.im.server.chat.protocol.MessageCodecSharable;
import com.hk.im.server.chat.protocol.ProtocolFrameDecoder;
import com.hk.im.server.chat.server.handler.common.HeartBeatEventHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : CustomProtocolChatChannelInitializer
 * @date : 2023/1/4 16:41
 * @description : 初始化handler
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CustomProtocolChatChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    // 维护与客户端的通道
    private NioSocketChannel ch;

    // handler 列表
    // 可重用，无状态，可共享处理器
    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

    QuitHandler QUIT_HANDLER = new QuitHandler();



    /**
     * 初始化通道处理器
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        this.ch = ch;
        this.doInitialization();
    }

    /**
     * 添加通道处理器
     * @throws Exception
     */
    private void doInitialization() throws Exception {
        // 用来判断是否是 读空闲时间过长，或者写空闲时间过长
        // 30分钟 内没有收到 channel 的数据，就会触发一个事件
        ch.pipeline().addLast(new IdleStateHandler(30,
                0, 0, TimeUnit.MINUTES));

        // ChannelDuplexHandler 可以同时作为入站和出战处理器
        ch.pipeline().addLast(new HeartBeatEventHandler());

        // 帧解码器
        ch.pipeline().addLast(new ProtocolFrameDecoder());
        ch.pipeline().addLast(LOGGING_HANDLER);
        ch.pipeline().addLast(MESSAGE_CODEC);


        // 处理消息
        ch.pipeline().addLast(QUIT_HANDLER);
    }

}
