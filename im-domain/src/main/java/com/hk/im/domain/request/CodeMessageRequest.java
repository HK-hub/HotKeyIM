package com.hk.im.domain.request;

import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.message.WebSocketMessage;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : CodeMessageRequest
 * @date : 2023/3/16 15:42
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CodeMessageRequest extends WebSocketMessage {

    // 发送者id
    private Long senderId;

    // 接收者id
    private Long receiverId;

    // 聊天类型：1.私聊，2.群聊
    private Integer talkType;

    // 代码内容
    private String code;

    // 语言类型:例如[java,c,cpp,python,go,js]等待
    private String language;

    // 代码块名称
    private String name;

    @Override
    public int getMessageActionType() {
        return MessageConstants.MessageActionType.CHAT.ordinal();
    }

    @Override
    public Integer getChatMessageType() {
        return MessageConstants.ChatMessageType.CODE.ordinal();
    }


}
