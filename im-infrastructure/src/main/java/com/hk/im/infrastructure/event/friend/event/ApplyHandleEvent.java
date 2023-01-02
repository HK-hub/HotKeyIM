package com.hk.im.infrastructure.event.friend.event;

import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : ApplyHandleEvent
 * @date : 2023/1/2 21:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ApplyHandleEvent extends AbstractEvent<ApplyHandleRequest> {
    public ApplyHandleEvent(Object source) {
        super(source);
    }

    public ApplyHandleEvent(Object source, ApplyHandleRequest request) {
        super(source);
        this.data = request;
    }

}
