package com.hk.im.service.worker;

import com.hk.im.client.service.SequenceService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.CodeMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.CodeMessageRequest;
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
 * @ClassName : CodeMessageWorker
 * @date : 2023/3/16 16:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class CodeMessageWorker {

    @Resource
    private SequenceService sequenceService;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private BaseMessageWorker baseMessageWorker;


    /**
     * 发送代码消息
     *
     * @param request
     *
     * @return
     */
    public ResponseResult sendMessage(CodeMessageRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getCode()) || Objects.isNull(request.getReceiverId())
                || Objects.isNull(request.getTalkType()) || StringUtils.isEmpty(request.getLanguage());
        if (BooleanUtils.isTrue(preCheck)) {
            // 消息参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 校验消息类型
        Integer chatMessageType = request.getChatMessageType();
        if (MessageConstants.ChatMessageType.CODE.ordinal() != chatMessageType) {
            // 不是文本消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        if (Objects.isNull(request.getSenderId())) {
            request.setSenderId(UserContextHolder.get().getId());
        }
        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();
        String code = request.getCode();
        Integer talkType = request.getTalkType();
        // 扩展信息
        CodeMessageExtra extra = new CodeMessageExtra();
        extra.setLang(request.getLanguage())
                .setName(request.getName())
                .setCode(code)
                .setLength(request.getCode().length());

        // 保存消息

        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(code)
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.CODE.ordinal())
                .setExtra(extra);
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
