package com.hk.im.server.signal.processor;

import com.alibaba.fastjson2.JSON;
import com.hk.im.server.common.bound.input.InboundMessageData;
import com.hk.im.server.signal.cmd.JoinRoomEventMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : JoinRoomEventProcessor
 * @date : 2023/3/23 10:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class JoinRoomEventProcessor extends BaseProcessor {


    /**
     * 处理加入房间消息
     *
     * @param ctx
     * @param inboundMessageData
     *
     * @return
     */
    public Object process(ChannelHandlerContext ctx, InboundMessageData inboundMessageData) {

        JoinRoomEventMessage joinRoomEvent = JSON.parseObject(inboundMessageData.getData(), JoinRoomEventMessage.class);
        log.info("handle join room event message:{}", joinRoomEvent);

        return null;
    }
}
