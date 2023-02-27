package com.hk.im.infrastructure.event.message.listener;

import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.client.service.RocketMQService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.constant.MessageQueueConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : MessageEventHandler
 * @date : 2023/2/24 11:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageEventHandler {

    @Resource
    private RocketMQService rocketMQService;
    @Resource
    private ChatCommunicationService chatCommunicationService;
    @Resource
    private ApplicationContext applicationContext;


    @Async
    @EventListener
    public void onSendMessage(SendChatMessageEvent event) {

        MessageBO messageBO = event.getData();

        // 判断消息是否存在
        if (Objects.isNull(messageBO)) {
            return;
        }

        // 判断消息状态
        Integer state = messageBO.getSendStatus();
        MessageConstants.SendStatusEnum sendStatus = MessageConstants.SendStatusEnum.values()[state];

        // 发送失败 or 草稿
        if (sendStatus == MessageConstants.SendStatusEnum.FAIL || sendStatus == MessageConstants.SendStatusEnum.DRAFT) {
            // 保存为会话草稿
            ResponseResult result = this.chatCommunicationService.updateCommunicationDraft(messageBO);
        }

        // 更新会话
        ResponseResult talkResult = this.chatCommunicationService.getChatCommunication(messageBO.getSenderId(), messageBO.getReceiverId());
        ChatCommunication communication = (ChatCommunication) talkResult.getData();
        communication.setLastMessageContent(messageBO.getContent())
                .setLastMessageId(messageBO.getId())
                .setLastSendTime(messageBO.getCreateTime());
        this.chatCommunicationService.updateById(communication);

        // 更新未读数目


        // 发送消息成功：发送MQ
        SendResult sendResult = this.rocketMQService.sendTagMsg(MessageQueueConstants.MessageConsumerTopic.chat_topic.topic,
                MessageConstants.ChatMessageType.TEXT.name(), messageBO);
        log.info("Sent message by rocketmq: {}", sendResult);
    }


}
