package com.hk.im.server.signal.processor.message;

import com.alibaba.fastjson.JSON;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.util.ObjectMapperUtil;
import com.hk.im.domain.message.control.MessageAckCallBackDomain;
import com.hk.im.domain.message.control.MessageAckDomain;
import com.hk.im.infrastructure.util.SpringUtils;
import com.hk.im.server.common.bound.input.InboundMessageData;
import com.hk.im.server.common.bound.output.OutboundMessageData;
import com.hk.im.server.common.channel.UserChannelManager;
import com.hk.im.server.common.constants.InboundDataType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageProcessor
 * @date : 2023/4/17 22:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MessageProcessor {

    /**
     * 消息已读
     * @param ctx
     * @param inboundMessageData
     * @return
     */
    public static Object msgReadEvent(ChannelHandlerContext ctx, InboundMessageData inboundMessageData) {

        // 序列化为消息确认实体
        MessageAckDomain ack = JSON.parseObject(inboundMessageData.getData(), MessageAckDomain.class);

        // 获取好友方
        Long receiverId = ack.getReceiver_id();
        // 获取消息id
        List<Long> ackMessageIdList = ack.getMsg_id();
        // 获取发送者
        Long senderId = ack.getSender_id();

        // 日志
        log.info("发送已读回执，通知发送方:receiverId={}, senderId={}, msgIds={}", receiverId, senderId, ackMessageIdList);

        // 发送事件，确认消息
        MessageFlowService messageFlowService = SpringUtils.getBean(MessageFlowService.class);
        ResponseResult<List<Long>> res = messageFlowService.ackChatMessage(senderId, receiverId, ackMessageIdList);

        // 发送已读回执，通知发送方
        if (res.isSuccess()) {
            // 通知发送方已读
            try {
                List<String> messageIdList = res.getData().stream().map(String::valueOf).toList();
                MessageAckCallBackDomain callBack = new MessageAckCallBackDomain().setIds(messageIdList)
                        .setSender_id(String.valueOf(senderId)).setReceiver_id(String.valueOf(receiverId));
                OutboundMessageData outbound = new OutboundMessageData()
                        .setEvent(InboundDataType.InboundEventTypeEnum.EVENT_TALK_READ.getEvent())
                        .setData(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(callBack));
                UserChannelManager.writeAndFlush(receiverId, outbound);
            } catch (Exception e) {
                log.info("send message ack read event failed: ", e);
            }
        }

        return res.getData();
    }


}
