package com.hk.im.infrastructure.event.message.listener;

import com.hk.im.client.service.*;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.bo.MessageRecordMemberBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.constant.MessageQueueConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.message.event.SendChatMessageEvent;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapstruct.MessageMapStructure;
import com.hk.im.infrastructure.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : MessageEventHandler
 * @date : 2023/2/24 11:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageEventHandler {

    @Resource
    private UserService userService;
    @Resource
    private UserManager userManager;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private RocketMQService rocketMQService;
    @Resource
    private ChatCommunicationService chatCommunicationService;
    @Resource
    private ApplicationContext applicationContext;


    @Async
    @EventListener
    public void onSendMessage(SendChatMessageEvent event) {

        MessageBO messageBO = event.getData();

        // 判断消息是否存在
        if (Objects.isNull(messageBO)) {
            return;
        }

        // 判断消息状态
        Integer state = messageBO.getSendStatus();
        MessageConstants.SendStatusEnum sendStatus = MessageConstants.SendStatusEnum.values()[state];

        // 发送失败 or 草稿
        if (sendStatus == MessageConstants.SendStatusEnum.FAIL || sendStatus == MessageConstants.SendStatusEnum.DRAFT) {
            // 保存为会话草稿
            ResponseResult result = this.chatCommunicationService.updateCommunicationDraft(messageBO);
            return ;
        }

        // 更新会话
        ResponseResult talkResult = this.chatCommunicationService.getChatCommunication(messageBO.getSenderId(), messageBO.getReceiverId());
        ChatCommunication communication = (ChatCommunication) talkResult.getData();
        communication.setLastMessageContent(messageBO.getContent())
                .setLastMessageId(messageBO.getId())
                .setLastSendTime(messageBO.getCreateTime());
        this.chatCommunicationService.updateById(communication);

        // 更新未读数目

        // 设置消息发送实体和接收实体
        MessageVO messageVO = this.computedMessageRecordMember(messageBO);

        // 发送消息成功：发送MQ
        // 计算发送的消息类型名称
        MessageConstants.ChatMessageType messageType = MessageConstants.ChatMessageType.values()[messageBO.getMessageType()];
        SendResult sendResult = this.rocketMQService.sendTagMsg(MessageQueueConstants.MessageConsumerTopic.chat_topic.topic,
                messageType.name(), messageVO);
        log.info("Sent message by rocketmq: {}", sendResult);
    }


    /**
     * 设置消息接收和发送的实体成员
     *
     * @param messageBO
     */
    private MessageVO computedMessageRecordMember(MessageBO messageBO) {

        Long senderId = messageBO.getSenderId();
        Long receiverId = messageBO.getReceiverId();

        // 获取发送者
        User sender = this.userService.getById(senderId);
        MessageRecordMemberBO senderMember = new MessageRecordMemberBO();
        MessageRecordMemberBO receiverMember = new MessageRecordMemberBO();
        UserVO friendVO = null;
        GroupMember groupMember = null;

                // 获取接收者：可能为群聊
        Integer chatType = messageBO.getChatType();
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == chatType) {
            // 私聊
            User receiver = this.userService.getById(receiverId);
            receiverMember.setUserId(receiverId)
                    .setUsername(receiver.getUsername())
                    .setRemarkName(receiver.getUsername())
                    .setAvatar(receiver.getMiniAvatar());
            // 查询朋友信息
            friendVO = this.userManager.findUserAndInfo(messageBO.getReceiverId());

        }else if (CommunicationConstants.SessionType.GROUP.ordinal() == chatType) {
            // 群聊
            groupMember = this.groupMemberService.getTheGroupMember(receiverId, senderId);
            // 设置发送者信息
            Group group = this.groupService.getById(receiverId);
            // 可能为@ 消息
            receiverMember.setGroupId(group.getId())
                    .setAvatar(group.getGroupAvatar())
                    .setUsername(group.getGroupName())
                    .setRemarkName(group.getGroupName());
            // 设置发送成员
            senderMember.setRemarkName(groupMember.getMemberRemarkName());

        }


        // 设置发送者信息
        senderMember.setUserId(sender.getId())
                .setAvatar(sender.getMiniAvatar())
                .setUsername(sender.getUsername());

        messageBO.setSenderMember(senderMember);
        messageBO.setReceiverMember(receiverMember);

        // 转换为 VO
        MessageVO messageVO = MessageMapStructure.INSTANCE.boToVO(messageBO);
        UserVO userVO = this.userManager.findUserAndInfo(messageBO.getSenderId());

        // 计算 userVO 和 friendVO 或者 GroupVO
        messageVO.computedPrivateMessageVO(userVO, friendVO, groupMember);

        // 响应数据
        return messageVO;
    }


}
