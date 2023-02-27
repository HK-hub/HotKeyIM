package com.hk.im.server.push.comsumer;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.push.worker.MessageOfflineWorker;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Set;

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
@RocketMQMessageListener(consumerGroup = "chat-message-group", topic = "chat-topic")
public class SendMessageConsumer implements RocketMQListener<MessageBO>, InitializingBean {

    /**
     * 发送消息
     * @param messageBO
     */
    @Override
    public void onMessage(MessageBO messageBO) {

        // 日志记录
        log.info("rocketmq on message(chat message): {}",  messageBO);

        // 进行推送消息
        Set<Channel> channelSet = UserChannelManager.getUserChannel(messageBO.getReceiverId());
        if (CollectionUtils.isEmpty(channelSet)) {
            // 离线消息处理
            MessageOfflineWorker.doProcess(messageBO);
            return;
        }

        // 1.判断消息talkType
        Integer chatType = messageBO.getChatType();
        // 2.离线userId-channel
        if (CommunicationConstants.SessionType.GROUP.ordinal() == chatType) {
            // 群聊类型
            for (Channel channel : channelSet) {
                if (channel.isActive()) {
                    // 推送消息: 给在线用户推送消息
                    channel.writeAndFlush(messageBO);
                }
            }

        } else if (CommunicationConstants.SessionType.PRIVATE.ordinal() == chatType) {
            // 私聊类型
            for (Channel channel : channelSet) {
                if (channel.isActive()) {
                    // 推送消息: 多端推送
                    channel.writeAndFlush(messageBO);
                }
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("sendMessage consumer bean created");
    }
}
