package com.hk.im.server.chat.server.handler.websocket;

import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.chat.server.handler.common.HeartBeatEventHandler;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : WebSocketServerChannelInitializer
 * @date : 2023/1/4 18:00
 * @description : 使用websocket 方式进行聊天服务
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class WebSocketServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    // 维护与客户端的通道
    private SocketChannel ch;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        this.ch = ch;
        ChannelPipeline pipeline = ch.pipeline();
        // 用来判断是否是 读空闲时间过长，或者写空闲时间过长,30分钟 内没有收到 channel 的数据，就会触发一个事件
        pipeline.addLast(new IdleStateHandler(MetaDataConfig.READ_IDEL_TIME_OUT, 0, 0, TimeUnit.MINUTES));
        // 心跳处理器，ChannelDuplexHandler 可以同时作为入站和出战处理器
        pipeline.addLast(new HeartBeatEventHandler());
        pipeline.addLast("http-codec",new HttpServerCodec());
        // 以块的形式写入
        pipeline.addLast("chunked-write", new ChunkedWriteHandler());
        // 对 HttpMessage 进行聚合处理，
        pipeline.addLast("aggregator",new HttpObjectAggregator(1024 * 64));
        // 压缩websocket 报文
        // pipeline.addLast(new WebSocketServerCompressionHandler());
        // 将自定义的 WebSocketFrame 放在 WebSocketServerProtocolHandler 前解决 netty 不支持 uri 上带参数的问题，造成的 websocket 连接失败
        pipeline.addLast("client-message-handler",new ClientMessageHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(MetaDataConfig.path, null, true));
        pipeline.addLast(new WebSocketCloseHandler());
    }
}
