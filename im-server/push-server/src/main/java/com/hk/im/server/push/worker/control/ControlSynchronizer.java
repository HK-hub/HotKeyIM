package com.hk.im.server.push.worker.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hk.im.common.util.ObjectMapperUtil;
import com.hk.im.domain.constant.CommunicationConstants;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.domain.vo.MessageVO;
import com.hk.im.domain.vo.message.RevokeMessageVO;
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
public class ControlSynchronizer {


    /**
     * 将撤回消息同步给好友
     * @param revokeMessageVO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeFriend(RevokeMessageVO revokeMessageVO) throws JsonProcessingException {

        log.info("synchronize revoke command to friend: {}", revokeMessageVO);
        MessageFlow flow = revokeMessageVO.getFlow();
        Set<Channel> channelSet = UserChannelManager.getUserChannel(flow.getReceiverId());

        // 构建消息数据
        DataContainer dataContainer = new DataContainer();
        dataContainer.setEvent(MessageConstants.MessageEventType.REVOKE.getEvent())
                .setData(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(revokeMessageVO));
        // 推送
        for (Channel channel : channelSet) {
            channel.writeAndFlush(MessageConverter.wrapperText(dataContainer));
        }
    }


    /**
     * 将撤回消息同步给群聊
     * @param revokeMessageVO
     */
    @Async("asyncServiceExecutor")
    public void synchronizeGroup(RevokeMessageVO revokeMessageVO) throws JsonProcessingException {

        log.info("synchronize revoke command to group: {}", revokeMessageVO);

        MessageFlow flow = revokeMessageVO.getFlow();
        Long groupId = flow.getReceiverId();
        // 获取到群聊成员Channel：在线群员
        Set<Channel> groupChannel = UserChannelManager.getGroupChannelExcludeMe(groupId, revokeMessageVO.getRevoker());

        // 构建消息数据
        DataContainer dataContainer = new DataContainer();
        dataContainer.setEvent(MessageConstants.MessageEventType.REVOKE.getEvent())
                .setData(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(revokeMessageVO));
        // 推送给在线群员
        for (Channel channel : groupChannel) {
            channel.writeAndFlush(MessageConverter.wrapperText(dataContainer));
        }
    }


    /**
     * 撤回消息
     * @param revokeMessageVO
     */
    public void synchronizedRevoke(RevokeMessageVO revokeMessageVO) throws JsonProcessingException {

        MessageFlow flow = revokeMessageVO.getFlow();
        Integer chatType = flow.getChatType();

        // 按照聊天类型进行同步推送
        if (chatType == CommunicationConstants.SessionType.PRIVATE.ordinal()) {
            // 私聊
            this.synchronizeFriend(revokeMessageVO);
        } else if (chatType == CommunicationConstants.SessionType.GROUP.ordinal()) {
            // 群聊
            this.synchronizeGroup(revokeMessageVO);
        }

    }


}
