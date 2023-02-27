package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.infrastructure.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
    implements ChatMessageService {

}




