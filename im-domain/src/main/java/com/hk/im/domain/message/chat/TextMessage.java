package com.hk.im.domain.message.chat;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : TextMessage
 * @date : 2023/2/23 21:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class TextMessage extends WebSocketMessage {

    private String senderId;

    private String receiverId;

    // 聊天类型：1.私聊，2.群聊
    private Integer talkType;

    private String groupId;

    private String text;

    @Override
    public int getMessageActionType() {
        return MessageConstants.MessageActionType.CHAT.ordinal();
    }

    @Override
    public Integer getChatMessageType() {
        return MessageConstants.ChatMessageType.TEXT.ordinal();
    }
}
