package com.hk.im.domain.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : HK意境
 * @ClassName : WebSocketMessage
 * @date : 2023/1/5 14:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class WebSocketMessage implements Serializable {

    // tcp 序列id
    protected long sequenceId;

    // 消息action类型
    protected int messageType;

    // 消息体
    protected String messageData;

    /**
     * 获取消息类型
     * @return
     */
    public int getMessageActionType() {
        return this.messageType;
    }


}
