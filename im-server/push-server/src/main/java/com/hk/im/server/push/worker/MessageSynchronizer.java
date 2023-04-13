package com.hk.im.server.push.worker;

import com.hk.im.client.service.GroupMemberService;
import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.vo.GroupMemberVO;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.infrastructure.util.SpringUtils;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.DataContainer;
import com.hk.im.server.common.message.MessageConverter;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
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
     * @param messageVO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeSelf(MessageVO messageVO) {

        Long senderId = messageVO.getSenderId();
        log.info("MessageSynchronizer synchronize message to me={},message={}", senderId, messageVO);

        // 推送给自己
        Set<Channel> channelSet = UserChannelManager.getUserChannel(senderId);
        if (CollectionUtils.isEmpty(channelSet)) {
            channelSet = Collections.emptySet();
        }
        this.doPushMessage(messageVO, channelSet);
    }


    /**
     * 将消息同步给好友
     * @param messageVO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeFriend(MessageVO messageVO) {


    }


    /**
     * 将消息同步给群聊
     * @param messageVO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeGroup(MessageVO messageVO) {

        Long groupId = messageVO.getReceiverId();
        log.info("MessageSynchronizer synchronize message group={},message={}", groupId, messageVO);

        // 获取到群聊成员Channel：在线群员
        Set<Channel> groupChannel = UserChannelManager.getGroupChannelExcludeMe(groupId, messageVO.getSenderId());

        // 推送给在线成员
        this.doPushMessage(messageVO, groupChannel);
    }


    /**
     * 消息推送：将消息推送到指定Channel 通道
     * @param messageVO
     * @param channelSet
     */
    public void doPushMessage(MessageVO messageVO, Set<Channel> channelSet) {

        // 好友在线，进行推送消息
        AbstractMessage message = MessageConverter.generateMessage(messageVO.getMessageType())
                .setMessageType(messageVO.getMessageType()).setMessageBO(messageVO);
        DataContainer dataContainer = new DataContainer().setMessage(message)
                .setActionType(MessageConstants.MessageActionType.CHAT.ordinal())
                .setEvent(MessageConstants.MessageEventType.CHAT.getEvent());
        // 推送消息
        for (Channel channel : channelSet) {
            if (channel.isActive()) {
                log.info("push message={} to Channel={}",dataContainer, channel.id().asLongText());
                channel.writeAndFlush(MessageConverter.wrapperText(dataContainer));
            }
        }

    }


}
