package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.MessageInviteService;
import com.hk.im.domain.entity.MessageInvite;
import com.hk.im.infrastructure.mapper.MessageInviteMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class MessageInviteServiceImpl extends ServiceImpl<MessageInviteMapper, MessageInvite>
    implements MessageInviteService {

}




