package com.hk.im.server.signal.processor;

import com.alibaba.fastjson2.JSON;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.infrastructure.util.SpringUtils;
import com.hk.im.server.common.bound.input.InboundMessageData;
import com.hk.im.server.common.event.SignalingEventMessage;
import com.hk.im.server.signal.constant.SignalingConstants;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : HK意境
 * @ClassName : BaseProcessor
 * @date : 2023/3/23 10:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class BaseProcessor {


    /**
     * 处理分发
     * @param ctx
     * @param inboundMessageData
     * @return
     */
    public static Object dispatchProcess(ChannelHandlerContext ctx, InboundMessageData inboundMessageData) {

        // 转换为SignalMessage
        SignalingEventMessage signalingEvent = JSON.parseObject(inboundMessageData.getData(), SignalingEventMessage.class);

        ResponseResult result = ResponseResult.SUCCESS();
        // 进行消息分派处理
        if (StringUtils.equals(SignalingConstants.SIGNALING_TYPE_JOIN, signalingEvent.getCmd())) {
            JoinRoomEventProcessor bean = SpringUtils.getBean(JoinRoomEventProcessor.class);
            bean.process(ctx, inboundMessageData);

        }

        return result;
    }




}
