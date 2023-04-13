package com.hk.im.service.worker;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.dto.ConversationMessageExtra;
import com.hk.im.domain.dto.FileMessageExtra;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.request.InviteVideoCallRequest;
import com.hk.im.infrastructure.event.file.event.UploadFileEvent;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : VideoMessageWorker
 * @date : 2023/3/22 15:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class VideoMessageWorker {

    @Resource
    private ApplicationContext applicationContext;


    /**
     * 处理邀请视频通话
     * @param request
     * @return
     */
    public ResponseResult inviteVideoCall(InviteVideoCallRequest request) {
        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getReceiverId()) || Objects.isNull(request.getTalkType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("附件消息参数错误!");
        }

        // 校验消息类型
        Integer chatMessageType = request.getChatMessageType();
        if (MessageConstants.ChatMessageType.VIDEO.ordinal() != chatMessageType) {
            // 不是音视频通话消息类型
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 素材准备
        Integer talkType = request.getTalkType();
        Long senderId = Long.valueOf(request.getSenderId());
        if (Objects.isNull(request.getSenderId())) {
            senderId = UserContextHolder.get().getId();
        }
        Long receiverId = Long.valueOf(request.getReceiverId());

        // 获取扩展信息
        ConversationMessageExtra extra = new ConversationMessageExtra()
                .setStatus(ConversationMessageExtra.ConversationStateEnum.WAIT.getStatus())
                .setState(ConversationMessageExtra.ConversationStateEnum.WAIT.getState())
                .setSenderId(senderId)
                .setParticipants(List.of(receiverId));

        // TODO 发布事件：保存云资源数据
        /*this.applicationContext.publishEvent(new UploadFileEvent(this, extra));
        // 保存消息
        ChatMessage chatMessage = new ChatMessage()
                // 消息内容
                .setContent(extra.getOriginalFileName())
                .setUrl(extra.getUrl())
                // 消息特性
                .setMessageFeature(MessageConstants.MessageFeature.DEFAULT.ordinal())
                // 消息类型
                .setMessageType(MessageConstants.ChatMessageType.VIDEO.ordinal())
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
        this.applicationContext.publishEvent(new SendChatMessageEvent(this, messageBO));*/

        // 发送消息成功
        return ResponseResult.SUCCESS(null).setMessage("消息发送成功!");
    }


    // 加入视频通话
    public ResponseResult joinVideoCall(InviteVideoCallRequest request) {
        return null;
    }

    // 拒绝视频通话
    public ResponseResult rejectVideoCall(InviteVideoCallRequest request) {

        return null;
    }



}
