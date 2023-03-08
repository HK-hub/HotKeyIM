package com.hk.im.server.push.worker;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.server.push.worker.pusher.MessageFriendPusher;
import com.hk.im.server.push.worker.pusher.MessageGroupPusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : MessagePushWorker
 * @date : 2023/2/24 20:54
 * @description : 消息推送工作者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessagePushWorker {

    @Resource
    private MessageFriendPusher messageFriendPusher;
    @Resource
    private MessageGroupPusher messageGroupPusher;


    /**
     * 消息推送处理：推送给自己(多端同步), 推送给好友，群聊
     * @param messageVO
     */
    public void doProcess(MessageVO messageVO) {
        // 聊天类型
        Integer chatType = messageVO.getChatType();
        // 根据聊天类型进行分发
        if (CommunicationConstants.SessionType.PRIVATE.ordinal() == chatType) {
            // 好友私聊
            this.messageFriendPusher.pushMessage(messageVO);
        } else if (CommunicationConstants.SessionType.GROUP.ordinal() == chatType) {
            // 群聊
            this.messageGroupPusher.pushMessage(messageVO);
        }

    }
}
