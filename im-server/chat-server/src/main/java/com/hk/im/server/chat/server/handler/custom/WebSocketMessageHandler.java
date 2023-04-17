package com.hk.im.server.chat.server.handler.custom;

import com.alibaba.fastjson2.JSON;
import com.hk.im.server.common.bound.input.InboundMessageData;
import com.hk.im.server.common.constants.InboundDataType;
import com.hk.im.server.signal.processor.BaseProcessor;
import com.hk.im.server.signal.processor.message.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : HK意境
 * @ClassName : WebSocketMessageHandler
 * @date : 2023/2/24 20:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class WebSocketMessageHandler {

    /**
     * 处理业务消息
     * @param ctx
     * @param frame
     */
    public static void process(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

        // 判断消息业务类型
        String text = frame.text();
        InboundMessageData inboundMessageData = JSON.parseObject(text, InboundMessageData.class);
        InboundDataType.InboundEventTypeEnum eventType = ensureDataType(inboundMessageData);
        log.info("InboundMessageData process:type={}, data={}", eventType.getEvent(), inboundMessageData);
        switch (eventType) {
            case DEFAULT_EVENT ->
                    log.info("Inbound data not matched event: {}", inboundMessageData);
            case SIGNALING_EVENT ->
                    // 信令事件
                    BaseProcessor.dispatchProcess(ctx, inboundMessageData);
            case HEAR_BEAT ->
                    // 心跳事件
                    BaseProcessor.hearBeatMessage(ctx, inboundMessageData);
            case EVENT_TALK_READ ->
                    // 消息已读通知
                    MessageProcessor.msgReadEvent(ctx,inboundMessageData);

        }
    }


    /**
     * 确定流入消息类型
     * @param inboundMessageData
     * @return
     */
    public static InboundDataType.InboundEventTypeEnum ensureDataType(InboundMessageData inboundMessageData) {

        String event = inboundMessageData.getEvent();
        InboundDataType.InboundEventTypeEnum eventType = InboundDataType.getEventType(event);
        return eventType;
    }



}
