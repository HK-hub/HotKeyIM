package com.hk.im.infrastructure.event.group.event;

import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : JoinGroupEvent
 * @date : 2023/2/14 12:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class JoinGroupEvent extends AbstractEvent<JoinGroupRequest> {
    public JoinGroupEvent(Object source) {
        super(source);
    }

    public JoinGroupEvent(Object source, JoinGroupRequest data) {
        super(source);
        this.data = data;
    }
}
