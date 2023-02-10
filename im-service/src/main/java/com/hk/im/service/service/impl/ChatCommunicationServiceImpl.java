package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.infrastructure.mapper.ChatCommunicationMapper;
import com.hk.im.service.service.ChatCommunicationService;
import org.springframework.stereotype.Service;

/**
 * @ClassName : ChatCommunicationServiceImpl
 * @author : HK意境
 * @date : 2023/2/10 18:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class ChatCommunicationServiceImpl extends ServiceImpl<ChatCommunicationMapper, ChatCommunication>
    implements ChatCommunicationService {


    /**
     * 创建会话
     * @param request
     * @return
     */
    @Override
    public ResponseResult createChatCommunication(CreateCommunicationRequest request) {
        return null;
    }
}




