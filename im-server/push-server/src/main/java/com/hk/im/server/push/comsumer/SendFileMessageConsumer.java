package com.hk.im.server.push.comsumer;

import com.hk.im.domain.vo.MessageVO;
import com.hk.im.server.push.worker.MessagePushWorker;
import com.hk.im.server.push.worker.MessageSynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SendFileMessageConsumer
 * @date : 2023/3/10 15:55
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "file-message-group", topic = "chat-topic", selectorExpression = "FILE")
public class SendFileMessageConsumer implements RocketMQListener<MessageVO> {


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
        log.info("rocketmq on message(chat message image): {}",  messageVO);

        // 进行消息同步
        this.messageSynchronizer.synchronizeSelf(messageVO);
        // 进行推送消息
        this.messagePushWorker.doProcess(messageVO);
    }
}
