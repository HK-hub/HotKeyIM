package com.hk.im.service.worker;

import com.hk.im.client.service.*;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.flow.data.sensitive.service.SensitiveWordService;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : TextMessageWorker
 * @date : 2023/3/9 21:54
 * @description : 文本消息处理者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class TextMessageWorker {

    @Resource
    private SensitiveWordService sensitiveWordService;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private BaseMessageWorker baseMessageWorker;

    /**
     * 处理文本消息发送业务
     * @param message
     * @return
     */
    public ResponseResult sendMessage(TextMessage message) {

        // 参数校验
        boolean preCheck = Objects.isNull(message) || StringUtils.isEmpty(message.getSenderId()) || Objects.isNull(message.getText()) ||
                StringUtils.isEmpty(message.getReceiverId()) || Objects.isNull(message.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 消息参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 校验消息类型
        Integer chatMessageType = message.getChatMessageType();
        if (MessageConstants.ChatMessageType.TEXT.ordinal() != chatMessageType) {
            // 不是文本消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Long senderId = Long.valueOf(message.getSenderId());
        Long receiverId = Long.valueOf(message.getReceiverId());
        String text = message.getText();
        Integer talkType = message.getTalkType();

        // 文本消息脱敏
        text = this.sensitiveWordService.sensitiveWords(text);

        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(text)
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.TEXT.ordinal());
        // 获取消息序列号
        ResponseResult sequenceResult = this.sequenceService.nextId(senderId, receiverId, talkType);
        if (BooleanUtils.isFalse(sequenceResult.isSuccess())) {
            // 获取序列号失败
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // 设置消息序列号
        chatMessage.setSequence((Long) sequenceResult.getData());
        // 消息流水
        MessageFlow messageFlow = new MessageFlow()
                .setSenderId(senderId).setReceiverId(receiverId)
                .setMessageType(chatMessage.getMessageType()).setChatType(talkType)
                .setSequence(chatMessage.getSequence())
                // 消息签收状态
                .setSignFlag(MessageConstants.SignStatsEnum.UNREAD.ordinal())
                // 消息发送状态
                .setSendStatus(MessageConstants.SendStatusEnum.SENDING.ordinal())
                .setDeleted(Boolean.FALSE);

        // 保存消息和消息流水
        MessageBO messageBO = this.baseMessageWorker.doSaveMessageAndFlow(chatMessage, messageFlow);
        // 判断消息发送是否成功
        if (Objects.isNull(messageBO)) {
            // 消息发送失败: 设置草稿->
            messageFlow.setSendStatus(MessageConstants.SendStatusEnum.FAIL.ordinal());
            messageBO = MessageMapStructure.INSTANCE.toBO(messageFlow, chatMessage);
            // TODO 发送消息保存事件
            this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));
            // 响应
            return ResponseResult.FAIL().setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发送消息保存事件
        this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));

        // 发送消息成功
        return ResponseResult.SUCCESS(messageBO).setMessage("消息发送成功!");
    }



}
