package com.hk.im.server.common.event;

import com.hk.im.server.common.message.AbstractMessage;
import com.hk.im.server.common.message.MessageTypeDeterminer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SimpleImageMessage
 * @date : 2023/3/10 10:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class SimpleImageMessage extends AbstractMessage {

    @Override
    public int getMessageType() {
        return MessageTypeDeterminer.ImageMessageType;
    }
}
