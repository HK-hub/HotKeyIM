package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.request.CreateCommunicationRequest;

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
}
