package com.hk.im.server.push.worker.signaling;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hk.im.common.util.ObjectMapperUtil;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.server.common.bound.output.OutboundMessageData;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.common.constants.OutboundDataEvent;
import com.hk.im.server.common.event.signaling.JoinRoomEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : SignalingEventPusher
 * @date : 2023/4/19 21:06
 * @description : 信令事件推送器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class SignalingEventPusher {

    public void pushVideoCallInvite(JoinRoomEventMessage message) {

        String receiverId = message.getReceiverId();
        OutboundMessageData outboundMessageData = new OutboundMessageData();
        try {
            outboundMessageData.setEvent(OutboundDataEvent.OutboundEventTypeEnum.SIGNALING_EVENT.getEvent())
                            .setData(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("send signaling event error on join video call: ", e);
        }
        UserChannelManager.writeAndFlush(Long.valueOf(receiverId), outboundMessageData);
    }


}
