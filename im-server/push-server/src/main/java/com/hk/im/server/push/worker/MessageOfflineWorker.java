package com.hk.im.server.push.worker;

import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.client.service.GroupMemberService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.ChatCommunication;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.server.common.channel.UserChannelManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : MessageOfflineWorker
 * @date : 2023/2/24 20:55
 * @description : 离线消息工作者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageOfflineWorker {

    @Resource
    private ChatCommunicationService chatCommunicationService;
    @Resource
    private GroupMemberService groupMemberService;

    /**
     * 离线消息处理: 处理会话未读数量, 这里是推送给好友的，
     * @param messageBO
     */
    public void processOfflineMessage(MessageBO messageBO) {
        log.info("offline message handle start:{}", messageBO);

        Long senderId = messageBO.getSenderId();
        Long receiverId = messageBO.getReceiverId();

        // 私聊群聊分别做对应会话的离线处理
        Integer talkType = messageBO.getChatType();
        if (talkType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
            // 私聊
            log.info("private offline message handle start");
            // 查询会话
            ResponseResult talkResult = this.chatCommunicationService.getChatCommunication(receiverId, senderId);
            if (BooleanUtils.isFalse(talkResult.isSuccess())) {
                // 离线消息处理失败
                log.info("offline message handle failed:{}",talkResult);
            }

            // 进行离线消息处理: 根据会话类型进行处理
            ChatCommunication talk = (ChatCommunication) talkResult.getData();
            this.doPrivate(messageBO, talk);
        } else if (talkType == CommunicationConstants.SessionType.GROUP.ordinal()){
            // TODO 群聊
            log.info("group offline message handle start");
            this.doGroup(messageBO);
        }
    }


    /**
     * 好友私聊离线消息处理:
     * 更新未读数量
     * @param talk
     */
    @Transactional
    public synchronized void doPrivate(MessageBO messageBO, ChatCommunication talk) {

        talk.setUnreadCount(talk.getUnreadCount() + 1);
        this.chatCommunicationService.updateById(talk);
    }

    /**
     * 群聊群成员离线消息处理
     * @param messageBO
     */
    public synchronized void doGroup(MessageBO messageBO) {

        // 离线用户做消息存储，未读处理
        Long groupId = messageBO.getReceiverId();

        // 获取群聊群成员;
        List<GroupMember> groupMemberIdList = groupMemberService.getGroupMemberIdList(groupId);
        Set<Long> onlineGroupMemberIdList = UserChannelManager.getGroupMemberIdSet(groupId);

        // 离线用户进行消息处理
        List<GroupMember> offlineGroupMemberIdList = groupMemberIdList.stream()
                // 在线群员id集合不包括当前群员，将会被选择出来
                .filter(member -> !onlineGroupMemberIdList.contains(member.getMemberId()))
                .collect(Collectors.toList());
        // 更新未读消息
        for (GroupMember member : offlineGroupMemberIdList) {
            // 查询会话
            ResponseResult talkResult = this.chatCommunicationService.getChatCommunication(member.getMemberId(), groupId);
            if (BooleanUtils.isTrue(talkResult.isSuccess())) {
                ChatCommunication talk = (ChatCommunication) talkResult.getData();

                talk.setUnreadCount(talk.getUnreadCount() + 1);
                // 用户会话更新
                this.chatCommunicationService.increaseTalkUnreadCount(talk,1);
            }
        }

    }


}
