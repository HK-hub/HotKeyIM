package com.hk.im.domain.request;

import com.hk.im.domain.bo.LocationBO;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : LocationMessageRequest
 * @date : 2023/4/13 21:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class LocationMessageRequest extends WebSocketMessage {

    private Long senderId;

    private Long receiverId;

    private Integer talkType;

    private Long groupId;

    private LocationBO location;


    @Override
    public int getMessageActionType() {
        return MessageConstants.MessageActionType.CHAT.ordinal();
    }

    @Override
    public Integer getChatMessageType() {
        return MessageConstants.ChatMessageType.LOCATION.ordinal();
    }

}
