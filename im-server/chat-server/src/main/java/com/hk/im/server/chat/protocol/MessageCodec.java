package com.hk.im.server.chat.protocol;


import com.hk.im.server.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageCodec
 * @date : 2022/12/28 0:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {


    /**
     * 编码器
     * @param channelHandlerContext
     * @param message
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        MessageCodecExecutor.encode(channelHandlerContext, message, out);
    }


    /**
     * 解码器
     * @param channelHandlerContext
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        MessageCodecExecutor.decode(channelHandlerContext, in, out);
    }


}
