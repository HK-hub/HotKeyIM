package com.hk.im.server.chat.protocol;

import com.hk.im.server.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageCodecSharable
 * @date : 2022/12/28 17:26
 * @description : 必须和 LengthFieldBasedFrameHandler 配合使用，确保接到的ByteBuf 是完整的
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        // 将 message 消息编码放入 buffer
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        MessageCodecExecutor.encode(channelHandlerContext, message,  buffer);

        // 传递
        list.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        // 解码
        MessageCodecExecutor.decode(channelHandlerContext, in,list);
    }
}
