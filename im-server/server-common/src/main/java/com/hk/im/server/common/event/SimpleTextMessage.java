package com.hk.im.server.common.event;

import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.MessageTypeDeterminer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SimpleTextMessage
 * @date : 2023/2/28 16:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SimpleTextMessage extends AbstractMessage {

    // 扩展信息
    private String extend;

    @Override
    public int getMessageType() {
        return MessageTypeDeterminer.TextMessageType;
    }
}
