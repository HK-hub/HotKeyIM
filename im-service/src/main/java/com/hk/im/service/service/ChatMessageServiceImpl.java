package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.infrastructure.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName : ChatMessageServiceImpl
 * @author : HK意境
 * @date : 2023/4/17 22:20
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
    implements ChatMessageService {

}




