package com.hk.im.server.common.message;

import com.alibaba.fastjson2.JSON;
import com.feilong.json.JsonUtil;
import com.hk.im.server.common.constants.MessageTypeConstants;
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

        MessageTypeConstants.MessageTypeEnum type = MessageTypeConstants.MessageTypeEnum.values()[messageType];

        // 返回消息类型
        return switch (type) {
            case TEXT -> new SimpleTextMessage();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }


    public static TextWebSocketFrame wrapperText(DataContainer dataContainer) {

        return new TextWebSocketFrame(JSON.toJSONString(dataContainer));
    }


}
