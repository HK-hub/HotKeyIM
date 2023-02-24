package com.hk.im.domain.message.system;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : NoSupportMessage
 * @date : 2023/1/5 19:36
 * @description : 非法操作消息
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class NoSupportMessage extends WebSocketMessage {

    private final String option = "抱歉，你的操作不合法或操作数据出现错误";
    private final LocalDateTime time = LocalDateTime.now();

    public NoSupportMessage() {
        this.messageActionType = MessageConstants.MessageActionType.NO_SUPPORT.ordinal();
    }

    @Override
    public int getMessageActionType() {
        return MessageConstants.MessageActionType.NO_SUPPORT.ordinal();
    }
}
