package com.hk.im.domain.request;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @author : HK意境
 * @ClassName : InviteVideoCallRequest
 * @date : 2023/3/22 15:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class InviteVideoCallRequest extends WebSocketMessage {

    // 聊天类型：1.私聊，2.群聊
    private Integer talkType;

    // 接收者id
    private String receiverId;

    private String senderId;


    @Override
    public int getMessageActionType() {
        return MessageConstants.MessageActionType.CHAT.ordinal();
    }

    @Override
    public Integer getChatMessageType() {
        return MessageConstants.ChatMessageType.VIDEO.ordinal();
    }

}
