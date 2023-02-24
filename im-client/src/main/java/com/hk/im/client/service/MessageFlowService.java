package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.dto.ChatMessageDTO;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.TalkRecordsRequest;

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
     * 发送文本消息
     * @param message
     * @return
     */
    ResponseResult sendTextMessage(TextMessage message);

    /**
     * 保存消息
     * @param message
     * @return
     */
    MessageBO doSaveMessageAndFlow(ChatMessage message, MessageFlow flow);

}
