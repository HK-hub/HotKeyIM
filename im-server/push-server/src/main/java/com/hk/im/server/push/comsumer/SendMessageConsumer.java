package com.hk.im.server.push.comsumer;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : SendMessageConsumer
 * @date : 2023/2/24 15:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.producer.group}", topic = MessageQueueConstants.CHAT_MESSAGE_TAG)
public class SendMessageConsumer implements RocketMQListener<MessageBO> {


    /**
     * 发送消息
     * @param messageBO
     */
    @Override
    public void onMessage(MessageBO messageBO) {

        // 日志记录
        log.info("rocketmq on message(chat message): {}",  messageBO);

        // 进行推送消息


    }
}
