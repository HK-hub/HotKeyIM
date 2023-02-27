package com.hk.im.server.chat.server.handler.custom;

import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.GroupMemberService;
import com.hk.im.client.service.GroupService;
import com.hk.im.client.service.RocketMQService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.constant.MessageQueueConstants;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.message.control.ConnectMessage;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.infrastructure.util.SpringUtils;
import com.hk.im.server.chat.config.MetaDataConfig;
import com.hk.im.server.common.channel.UserChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : WebSocketConnectHandler
 * @date : 2023/2/24 20:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class WebSocketConnectHandler {

    /**
     * 连接处理
     * BUG: 这里request.uri() 是请求路径上得参数，参数被编码了，需要自己转码
     *
     * @param ctx
     * @param msg
     */
    public static void doConnect(ChannelHandlerContext ctx, Object msg) {
        // 第一次的连接请求，请求升级为Websocket
        FullHttpRequest request = (FullHttpRequest) msg;
        if (Objects.isNull(request)) {
            ctx.close();
        }
        String uri = request.uri();
        log.info("request:uri={},headers={},method={}", uri, request.headers().get("Origin"), request.method());
        if (StringUtils.isEmpty(request.headers().get("Origin"))) {
            log.info("request origin is empty");
            ctx.close();
        }

        // 处理请求参数
        String path = null;
        String token = null;
        if (StringUtils.contains(uri, ":")) {
            // Map<String, String> urlParams = getUrlParams(uri);
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
            try {
                String[] split = uri.split(":");
                path = split[0];
                token = split[1];
                log.info("request url params:token={}", token);
                request.setUri(path);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.close();
            }
        }

        // 验证 usi
        log.info("the FullHttpRequest new uri={}", request.uri());
        if (!Objects.equals(request.uri(), MetaDataConfig.path)) {
            // 请求路径错误: 关闭连接
            log.info("the websocket path is error: expect {}, but provide {}", request.uri(), MetaDataConfig.path);
            ctx.channel().close();
        }

        // 认证处理
        AuthorizationService authorizationService = SpringUtils.getBean(AuthorizationService.class);
        User user = authorizationService.authUserByToken(token);
        if (Objects.isNull(user)) {
            // 认证失败
            log.info("this websocket owner is not authorized:{}", ctx.channel().id().asLongText());
            ctx.channel().close();
        }

        // TODO 认证成功，发布事件消息
        RocketMQService rocketMQService = SpringUtils.getBean(RocketMQService.class);
        rocketMQService.sendTagMsg(MessageQueueConstants.MessageConsumerTopic.connect_topic.topic,
                MessageQueueConstants.MessageConsumerTag.connect_tag.tag,
                new ConnectMessage().setUserId(user.getId()).setToken(token));

        // 添加 channel
        setUserChannel(user.getId(), ctx.channel());
    }


    /**
     * 设置用户channel 通道：个人，群聊
     *
     * @param userId
     * @param channel
     */
    public static void setUserChannel(Long userId, Channel channel) {

        // 1.加入用户 chanel 管理组
        UserChannelManager.add(userId, channel);

        // 2.加入群聊channel管理组
        // 查询用户加入群聊
        GroupService groupService = SpringUtils.getBean(GroupService.class);
        ResponseResult result = groupService.getUserJoinGroupList(String.valueOf(userId));

        if (BooleanUtils.isFalse(result.isSuccess())) {
            // 查询群聊失败
            return;
        }

        // 设置群聊channel
        List<GroupVO> groupVOList = (List<GroupVO>) result.getData();
        for (GroupVO groupVO : groupVOList) {
            UserChannelManager.addGroupChannel(groupVO.getId(), channel);
        }
    }


}
