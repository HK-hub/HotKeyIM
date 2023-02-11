package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.entity.MessageFlow;

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
}
