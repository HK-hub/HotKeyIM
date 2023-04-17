package com.hk.im.server.common.message;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.im.common.util.ObjectMapperUtil;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.server.common.constants.MessageTypeConstants;
import com.hk.im.server.common.event.SimpleFileMessage;
import com.hk.im.server.common.event.SimpleImageMessage;
import com.hk.im.server.common.event.SimpleLocationMessage;
import com.hk.im.server.common.event.SimpleTextMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author : HK意境
 * @ClassName : MessageConverter
 * @date : 2023/2/28 19:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageConverter {

    public static AbstractMessage generateMessage(Integer messageType) {

        MessageConstants.ChatMessageType type = MessageConstants.ChatMessageType.values()[messageType];

        // 返回消息类型
        return switch (type) {
            case TEXT -> new SimpleTextMessage();
            case IMAGE -> new SimpleImageMessage();
            case CODE ->  new SimpleFileMessage();
            case FILE ->  new SimpleFileMessage();
            case LOCATION -> new SimpleLocationMessage();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }


    public static TextWebSocketFrame wrapperText(DataContainer dataContainer) {

        // 使用 Jackson 序列化
        try {
            return new TextWebSocketFrame(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(dataContainer));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static TextWebSocketFrame wrapperText(Object object) {

        // 使用 Jackson 序列化
        try {
            return new TextWebSocketFrame(ObjectMapperUtil.OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }



}
