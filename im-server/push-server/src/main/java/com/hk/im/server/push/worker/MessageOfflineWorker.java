package com.hk.im.server.push.worker;

import com.hk.im.client.service.ChatCommunicationService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.entity.ChatCommunication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


    /**
     * 离线消息处理: 处理会话未读数量, 这里是推送给好友的，
     * @param messageBO
     */
    public void doProcess(MessageBO messageBO) {
        log.info("offline message handle start:{}", messageBO);

        Long senderId = messageBO.getSenderId();
        Long receiverId = messageBO.getReceiverId();
        // 查询会话
        ResponseResult talkResult = this.chatCommunicationService.getChatCommunication(senderId, receiverId);
        if (BooleanUtils.isFalse(talkResult.isSuccess())) {
            // 离线消息处理失败
            log.info("offline message handle failed:{}",talkResult);
        }

        // 进行离线消息处理: 根据会话类型进行处理
        ChatCommunication talk = (ChatCommunication) talkResult.getData();

        Integer talkType = talk.getSessionType();
        if (talkType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
            // 私聊
            this.doPrivate(messageBO, talk);
        } else {
            // TODO 群聊
        }


    }


    /**
     * 好友私聊离线消息处理:
     * 更新未读数量
     * @param talk
     */
    public synchronized void doPrivate(MessageBO messageBO, ChatCommunication talk) {

        talk.setUnreadCount(talk.getUnreadCount() + 1);
        this.chatCommunicationService.updateById(talk);
    }


}
