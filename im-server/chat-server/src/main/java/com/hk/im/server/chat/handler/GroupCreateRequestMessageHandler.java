package com.hk.im.server.chat.handler;


import com.hk.im.server.chat.message.GroupCreateRequestMessage;
import com.hk.im.server.chat.message.GroupCreateResponseMessage;
import com.hk.im.server.chat.session.Group;
import com.hk.im.server.chat.session.GroupSession;
import com.hk.im.server.chat.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : GroupCreateRequestMessageHandler
 * @date : 2022/12/28 20:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();

        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);

        if (Objects.isNull(group)) {

            // 发送成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
            // 创建群聊成功, 发送拉群消息: 在线用户才会发送拉群消息
            List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
            for (Channel channel : membersChannel) {
                channel.writeAndFlush("您已被拉入" + groupName);
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }

    }
}
