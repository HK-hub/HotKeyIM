package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.request.ClearUnreadRequest;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.domain.request.TopTalkRequest;

/**
 * @ClassName : ChatCommunicationService
 * @author : HK意境
 * @date : 2023/2/10 17:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface ChatCommunicationService extends IService<ChatCommunication> {


    /**
     * 创建会话
     * @param request
     * @return
     */
    ResponseResult createChatCommunication(CreateCommunicationRequest request);

    /**
     * 判断会话是否存在
     * @param senderId
     * @param receiverId
     * @return
     */
    boolean existsChatCommunication(Long senderId, Long receiverId);

    /**
     * 获取会话
     * @param senderId
     * @param receiverId
     * @return
     */
    ResponseResult getChatCommunication(Long senderId, Long receiverId);


    /**
     * 获取用户会话列表
     * @param userId
     * @return
     */
    ResponseResult getUserCommunicationList(Long userId);


    /**
     * 更新会话草稿内容
     * @param messageBO
     * @return
     */
    ResponseResult updateCommunicationDraft(MessageBO messageBO);

    /**
     * 置顶会话、取消置顶
     * @param request
     * @return
     */
    ResponseResult topTalkCommunication(TopTalkRequest request);

    /**
     * 清空消息未读数量
     * @param request
     * @return
     */
    ResponseResult clearUnreadMessage(ClearUnreadRequest request);


    /**
     * 获取自己的会话
     * @param senderId
     * @return
     */
    ChatCommunication getMyselfCommunication(Long senderId);
}
