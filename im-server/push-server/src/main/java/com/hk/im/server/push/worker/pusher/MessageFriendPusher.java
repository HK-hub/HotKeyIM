package com.hk.im.server.push.worker.pusher;

import com.feilong.lib.javassist.Loader;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.common.event.SimpleTextMessage;
import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.DataContainer;
import com.hk.im.server.common.message.MessageConverter;
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
     * @param messageBO
     */
    public void pushMessage(MessageBO messageBO) {

        // 获取好友通道
        Set<Channel> channelSet = UserChannelManager.getUserChannel(messageBO.getReceiverId());
        if (CollectionUtils.isEmpty(channelSet)) {
            // 离线消息处理
            this.messageOfflineWorker.doProcess(messageBO);
            return;
        }

        // 推送
        this.messageSynchronizer.doPushMessage(messageBO, channelSet);

    }

}
