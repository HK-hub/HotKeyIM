package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.service.service.GroupService;
import com.hk.im.service.service.MessageFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 */
@Service
public class MessageFlowServiceImpl extends ServiceImpl<MessageFlowMapper, MessageFlow>
        implements MessageFlowService {

    @Resource
    private MessageFlowMapper messageFlowMapper;
    @Resource
    private GroupService groupService;


    /**
     * 获取会话最大消息Sequence
     * @param senderId
     * @param receiverId
     * @return
     */
    @Override
    public MessageFlow getCommunicationMaxMessageSequence(Long senderId, Long receiverId) {

        // 根据 receiverId 是否为群群聊
        Group group = this.groupService.getById(receiverId);
        if (Objects.nonNull(group)) {
            // 群聊，只需要获取receiverId 最大消息
            MessageFlow messageFlow = this.messageFlowMapper.selectGroupMaxMessageSequence(senderId, receiverId);
            // 响应处理
            return messageFlow;
        }

        // 好友私聊, 需要查询双向消息中较大的消息ID
        MessageFlow messageFlow = this.messageFlowMapper.selectPrivateMaxMessageSequence(senderId, receiverId);
        // 响应处理
        return messageFlow;
    }
}




