package com.hk.im.server.push.comsumer.signaling;

import com.hk.im.domain.request.InviteVideoCallInviteRequest;
import com.hk.im.server.common.event.signaling.JoinRoomEventMessage;
import com.hk.im.server.push.worker.signaling.SignalingEventPusher;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : VideoCallInviteConsumer
 * @date : 2023/4/19 21:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "signaling-video-group", topic = "signaling-topic",
        selectorExpression = "start", selectorType = SelectorType.TAG)
public class VideoCallInviteConsumer implements RocketMQListener<InviteVideoCallInviteRequest>, InitializingBean {


    @Resource
    private SignalingEventPusher signalingEventPusher;

    /**
     * 消费消息
     * @param request
     */
    @Override
    public void onMessage(InviteVideoCallInviteRequest request) {

        // 日志记录
        log.info("rocketmq on message(signaling:video request text): {}",  request);

        // 构建消息
        Long dialer = Long.valueOf(request.getDialer());
        Long listener = Long.valueOf(request.getListener());
        String roomId = dialer < listener ? dialer + "-" + listener : listener + "-" + dialer;
        JoinRoomEventMessage joinRoomEventMessage = new JoinRoomEventMessage()
                .setRoomId(roomId).setType(request.getType())
                .setReceiverId(request.getListener()).setUserId(request.getDialer());
        joinRoomEventMessage.setCmd(request.getCmd());
        // 进行推送消息
        this.signalingEventPusher.pushVideoCallInvite(joinRoomEventMessage);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("signaling consumer bean created");
    }

}
