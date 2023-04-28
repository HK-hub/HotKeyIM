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

        AbstractMessage message = null;
        // 返回消息类型
        switch (type) {
            case TEXT :
                message = new SimpleTextMessage();
                break;
            case IMAGE :
                message = new SimpleImageMessage();
                break;
            case CODE :
                message = new SimpleFileMessage();
                break;
            case FILE :
                message = new SimpleFileMessage();
                break;
            case LOCATION :
                message = new SimpleLocationMessage();
                break;
            default :
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return message;
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
