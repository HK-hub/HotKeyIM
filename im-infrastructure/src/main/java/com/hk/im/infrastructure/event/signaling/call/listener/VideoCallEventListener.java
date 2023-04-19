package com.hk.im.infrastructure.event.signaling.call.listener;

import com.hk.im.client.service.RocketMQService;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.constant.MessageQueueConstants;
import com.hk.im.domain.request.InviteVideoCallInviteRequest;
import com.hk.im.infrastructure.event.signaling.call.event.VideoCallInviteEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : VideoCallEventListener
 * @date : 2023/4/19 20:39
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class VideoCallEventListener {


    @Resource
    private RocketMQService rocketMQService;

    /**
     * 处理邀请事件
     * @param event
     */
    @Async
    @EventListener()
    public void handleVideoCallInvite(VideoCallInviteEvent event) {

        InviteVideoCallInviteRequest request = event.getData();

        // 发送消息到消息队列，让推送服务器进行消费

        SendResult sendResult = this.rocketMQService.sendTagMsg(MessageQueueConstants.MessageConsumerTopic.signaling_topic.topic,
                request.getCmd(), request);
        log.info("video call event={}, by rocketmq result: {}", request, sendResult);
    }


}
