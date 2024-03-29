package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.message.DeleteRecordsRequest;
import com.hk.im.domain.request.message.RevokeMessageRequest;
import com.hk.im.domain.vo.MessageVO;

import java.util.List;

/**
 * @ClassName : MessageFlowService
 * @author : HK意境
 * @date : 2023/2/11 14:49
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface MessageFlowService extends IService<MessageFlow> {

    /**
     * 获取会话下的最大消息ID
     * @param senderId
     * @param receiverId
     * @return
     */
    MessageFlow getCommunicationMaxMessageSequence(Long senderId, Long receiverId);


    /**
     * 获取最新的聊天记录
     * @param request
     * @return
     */
    ResponseResult getLatestTalkRecords(TalkRecordsRequest request);


    /**
     * 分页获取聊天记录
     * @param request
     * @return
     */
    ResponseResult getTalkRecordsByPage(TalkRecordsRequest request);

    /**
     * 转换为消息VO对象
     * @param messageFlow
     * @return
     */
    MessageVO convertToMessageVO(MessageFlow messageFlow);


    /**
     * 发送文本消息
     * @param message
     * @return
     */
    ResponseResult sendTextMessage(TextMessage message);

    /**
     * 发送图片消息
     * @param request
     * @return {@link ResponseResult}
     */
    ResponseResult sendImageMessage(ImageMessage request);


    /**
     * 发送附件消息
     * @param request
     * @return
     */
    ResponseResult sendAttachmentMessage(AttachmentMessage request);

    /**
     * 发送代码消息
     * @param request
     * @return
     */
    ResponseResult sendCodeMessage(CodeMessageRequest request);

    /**
     * 发起视频通话
     * @param request
     * @return
     */
    ResponseResult sendVideoMessage(InviteVideoCallRequest request);

    /**
     * 发送位置消息
     * @param request
     * @return
     */
    ResponseResult sendLocationMessage(LocationMessageRequest request);

    /**
     * 确认消息
     * @param receiverId
     * @param ackMessageIdList
     * @param senderId
     * @return
     */
    ResponseResult ackChatMessage(Long senderId, Long receiverId, List<Long> ackMessageIdList);

    /**
     * 发起视频通话邀请
     * @param request
     * @return
     */
    ResponseResult sendVideoInviteMessage(InviteVideoCallInviteRequest request);

    /**
     * 撤回消息
     * @param request
     * @return
     */
    ResponseResult revokeMessage(RevokeMessageRequest request);

    ResponseResult deleteMessageRecords(DeleteRecordsRequest request);
}
