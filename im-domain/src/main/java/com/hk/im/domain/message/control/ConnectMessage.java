package com.hk.im.domain.message.control;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : ConnectMessage
 * @date : 2023/1/5 17:45
 * @description : 用户连接服务器消息
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class ConnectMessage extends WebSocketMessage {

    // 当前用户id
    private Long userId;

    // 当前请求连接的用户的 token
    private String token;

    // 扩展信息
    private String extend;

    /**
     * 响应消息类型
     * @return
     */
    @Override
    public int getMessageActionType() {
        // MESSAGE_CLASSES_MAP.put(MessageConstants.MessageActionType.CONNECT.ordinal(), ConnectMessage.class);
        return MessageConstants.MessageActionType.CONNECT.ordinal();
    }
}
