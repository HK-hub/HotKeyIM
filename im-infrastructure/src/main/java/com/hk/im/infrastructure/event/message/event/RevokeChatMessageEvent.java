package com.hk.im.infrastructure.event.message.event;

import com.hk.im.domain.bo.MessageBO;
import com.hk.im.domain.vo.message.RevokeMessageVO;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : RevokeChatMessageEvent
 * @date : 2023/5/3 19:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class RevokeChatMessageEvent extends AbstractEvent<RevokeMessageVO> {
    public RevokeChatMessageEvent(Object source) {
        super(source);
    }

    public RevokeChatMessageEvent(Object source, RevokeMessageVO revokeMessageVO) {
        super(source);
        this.data = revokeMessageVO;
    }

}
