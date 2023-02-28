package com.hk.im.server.push.worker;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.DataContainer;
import com.hk.im.server.common.message.MessageConverter;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * @author : HK意境
 * @ClassName : MessageSynchronizer
 * @date : 2023/2/24 20:56
 * @description : 消息同步工作者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageSynchronizer {


    /**
     * 同步消息给自己
     * @param messageBO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeSelf(MessageBO messageBO) {

        Long senderId = messageBO.getSenderId();
        log.info("MessageSynchronizer synchronize message to me={},message={}", senderId, messageBO);

        // 推送给自己
        Set<Channel> channelSet = UserChannelManager.getUserChannel(senderId);
        if (CollectionUtils.isEmpty(channelSet)) {
            channelSet = Collections.emptySet();
        }
        this.doPushMessage(messageBO, channelSet);
    }


    public void doPushMessage(MessageBO messageBO, Set<Channel> channelSet) {

        // 好友在线，进行推送消息
        AbstractMessage message = MessageConverter.generateMessage(messageBO.getMessageType());

        DataContainer dataContainer = new DataContainer().setMessage(message)
                .setActionType(MessageConstants.MessageActionType.CHAT.ordinal());
        // 推送消息
        for (Channel channel : channelSet) {
            if (channel.isActive()) {
                log.info("push message={} to Channel={}",dataContainer, channel.id().asLongText());
                channel.writeAndFlush(dataContainer);
            }
        }

    }


}
