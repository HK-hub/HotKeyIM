package com.hk.im.server.chat.handler;


import com.hk.im.server.chat.message.ChatRequestMessage;
import com.hk.im.server.chat.message.ChatResponseMessage;
import com.hk.im.server.chat.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : ChatRequestMessageHandler
 * @date : 2022/12/28 20:42
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel toChannel = SessionFactory.getSession().getChannel(to);

        if (Objects.isNull(toChannel)) {
            // 对方未上线
            // 存储到数据库，等待对方上线, 主动拉去，推送
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方用户不在线或不存在"));

        } else {
            // 对方在线
            toChannel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        }

    }
}
