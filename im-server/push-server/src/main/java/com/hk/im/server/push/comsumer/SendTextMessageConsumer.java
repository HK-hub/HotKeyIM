package com.hk.im.server.push.comsumer;

import com.hk.im.domain.vo.MessageVO;
import com.hk.im.server.push.worker.MessagePushWorker;
import com.hk.im.server.push.worker.MessageSynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SendTextMessageConsumer
 * @date : 2023/2/24 15:36
 * @description : 发送聊天消息消费者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "chat-message-group", topic = "chat-topic",
        selectorExpression = "TEXT", selectorType = SelectorType.TAG)
public class SendTextMessageConsumer implements RocketMQListener<MessageVO>, InitializingBean {

    @Resource
    private MessagePushWorker messagePushWorker;
    @Resource
    private MessageSynchronizer messageSynchronizer;

    /**
     * 发送消息
     * @param messageVO
     */
    @Override
    public void onMessage(MessageVO messageVO) {

        // 日志记录
        log.info("rocketmq on message(chat message text): {}",  messageVO);

        // 进行消息同步
         this.messageSynchronizer.synchronizeSelf(messageVO);
        // 进行推送消息
         this.messagePushWorker.doProcess(messageVO);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("sendMessage consumer bean created");
    }
}
