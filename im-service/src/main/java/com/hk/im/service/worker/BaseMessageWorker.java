package com.hk.im.service.worker;

import com.hk.im.client.service.*;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.MessageFlowMapper;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : BaseMessageWorker
 * @date : 2023/3/9 21:56
 * @description : 公共基础消息处理者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class BaseMessageWorker {

    @Resource
    private MessageFlowService messageFlowService;
    @Resource
    private ChatMessageService chatMessageService;


    /**
     * 保存消息和消息流水
     *
     * @param message
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MessageBO doSaveMessageAndFlow(ChatMessage message, MessageFlow flow) {

        // 保存消息
        boolean save = this.chatMessageService.save(message);
        if (BooleanUtils.isFalse(save)) {
            // 保存消息失败
            return null;
        }

        // 设置消息流水
        flow.setMessageId(message.getId())
                .setCreateTime(message.getCreateTime())
                .setUpdateTime(message.getUpdateTime())
                .setSendStatus(MessageConstants.SendStatusEnum.SENDED.ordinal());
        Integer chatType = flow.getChatType();
        if (chatType == CommunicationConstants.SessionType.GROUP.ordinal()) {
            // 群聊类型
            flow.setGroupId(flow.getReceiverId());
        }

        // 保存消息流水
        boolean flowSave = this.messageFlowService.save(flow);
        if (BooleanUtils.isFalse(flowSave)) {
            // 保存失败
            return null;
        }

        // 更新会话状态

        // 响应DTO
        return MessageMapStructure.INSTANCE.toBO(flow, message);
    }



}
