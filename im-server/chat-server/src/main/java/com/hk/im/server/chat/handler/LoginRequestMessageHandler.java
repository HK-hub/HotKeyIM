package com.hk.im.server.chat.handler;


import com.hk.im.server.chat.message.LoginRequestMessage;
import com.hk.im.server.chat.message.LoginResponseMessage;
import com.hk.im.server.chat.service.UserServiceFactory;
import com.hk.im.server.chat.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : LoginRequestMessageHandler
 * @date : 2022/12/28 20:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {

        log.info("收到登录请求：{}", msg);
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);

        // 判断登录结果
        LoginResponseMessage message = null;
        if (login) {
            // 登录成功
            message = new LoginResponseMessage(true, "登录成功");
            SessionFactory.getSession().bind(ctx.channel(), username);

        } else {
            message = new LoginResponseMessage(false, "用户名或密码错误");
        }
        ctx.writeAndFlush(message);
    }
}
