package com.hk.im.server.chat.server.handler.websocket;

import com.hk.im.server.chat.config.MetaDataConfig;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author : HK意境
 * @ClassName : WebSocketProtocolChatChannelInitializer
 * @date : 2023/1/4 18:00
 * @description : 使用websocket 方式进行聊天服务
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class WebSocketProtocolChatChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    // 维护与客户端的通道
    private SocketChannel ch;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        this.ch = ch;
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if(msg instanceof FullHttpRequest) {
                    FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
                    String uri = fullHttpRequest.uri();
                    if (!uri.equals(MetaDataConfig.path)) {
                        // 访问的路径不是 websocket的端点地址，响应404
                        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
                                .addListener(ChannelFutureListener.CLOSE);
                        return ;
                    }
                }
                super.channelRead(ctx, msg);
            }
        });
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(MetaDataConfig.path, null, true, MetaDataConfig.maxFrameSize));
        pipeline.addLast(new WebSocketMessageHandler());
        pipeline.addLast(new WebSocketCloseHandler());
    }
}
