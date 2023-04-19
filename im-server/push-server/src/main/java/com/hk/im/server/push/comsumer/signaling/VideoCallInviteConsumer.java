package com.hk.im.server.push.comsumer.signaling;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.springframework.stereotype.Component;

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
@RocketMQMessageListener(consumerGroup = "code-message-group", topic = "chat-topic",
        selectorExpression = "CODE", selectorType = SelectorType.TAG)
public class VideoCallInviteConsumer {
}
