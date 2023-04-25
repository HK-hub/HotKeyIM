package com.hk.im.infrastructure.event.group.listener;

import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.infrastructure.event.group.event.JoinGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : GroupEventListener
 * @date : 2023/2/14 12:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class GroupEventListener {

    @Resource
    private ChatCommunicationService chatCommunicationService;
    @Resource
    private MessageFlowService messageFlowService;

    /**
     * 处理加入群聊事件
     * @param event
     */
    @Async
    @EventListener
    public void joinGroupEventHandler(JoinGroupEvent event) {

        JoinGroupRequest joinGroup = event.getData();

        // 创建会话：
        CreateCommunicationRequest request = new CreateCommunicationRequest().setType(CommunicationConstants.SessionType.GROUP.ordinal())
                .setUserId(joinGroup.getUserId()).setReceiverId(joinGroup.getGroupId());
        this.chatCommunicationService.createChatCommunication(request);

        // 发送消息
        TextMessage textMessage = new TextMessage()
                .setText("大家好，请多多关照!")
                .setSenderId(joinGroup.getUserId())
                .setTalkType(CommunicationConstants.SessionType.GROUP.ordinal())
                .setGroupId(joinGroup.getGroupId())
                .setReceiverId(joinGroup.getGroupId());
        this.messageFlowService.sendTextMessage(textMessage);

    }


}
