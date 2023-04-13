package com.hk.im.server.push.worker.pusher;

import com.hk.im.domain.vo.MessageVO;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.push.worker.MessageOfflineWorker;
import com.hk.im.server.push.worker.MessageSynchronizer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : MessageFriendPusher
 * @date : 2023/2/28 16:47
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageFriendPusher {

    @Resource
    private MessageOfflineWorker messageOfflineWorker;
    @Resource
    private MessageSynchronizer messageSynchronizer;

    /**
     * 好友私聊
     * @param messageVO
     */
    public void pushMessage(MessageVO messageVO) {

        // 获取好友通道
        Set<Channel> channelSet = UserChannelManager.getUserChannel(messageVO.getReceiverId());
        if (CollectionUtils.isEmpty(channelSet)) {
            // 离线消息处理
            this.messageOfflineWorker.processOfflineMessage(messageVO);
            return;
        }

        // 推送
        this.messageSynchronizer.doPushMessage(messageVO, channelSet);

    }

}
