package com.hk.im.server.push.comsumer;

import com.alibaba.fastjson2.JSON;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.push.worker.MessageOfflineWorker;
import com.hk.im.server.push.worker.MessagePushWorker;
import com.hk.im.server.push.worker.MessageSynchronizer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Resource
    private MessagePushWorker messagePushWorker;
    @Resource
    private MessageSynchronizer messageSynchronizer;

    /**
     * 发送消息
     * @param messageBO
     */
    @Override
    public void onMessage(MessageBO messageBO) {

        // 日志记录
        log.info("rocketmq on message(chat message): {}",  messageBO);

        // 进行消息同步
        this.messageSynchronizer.synchronizeSelf(messageBO);
        // 进行推送消息
        this.messagePushWorker.doProcess(messageBO);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("sendMessage consumer bean created");
    }
}
