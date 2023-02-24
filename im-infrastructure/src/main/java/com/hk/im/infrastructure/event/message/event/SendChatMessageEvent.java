package com.hk.im.infrastructure.event.message.event;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : SendChatMessageEvent
 * @date : 2023/2/23 23:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class SendChatMessageEvent extends AbstractEvent<MessageBO> {
    public SendChatMessageEvent(Object source) {
        super(source);
    }

    public SendChatMessageEvent(Object source, MessageBO messageBO) {
        super(source);
        this.data = messageBO;
    }


}
