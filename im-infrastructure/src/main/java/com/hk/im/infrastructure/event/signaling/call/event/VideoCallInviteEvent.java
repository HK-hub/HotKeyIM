package com.hk.im.infrastructure.event.signaling.call.event;

import com.hk.im.domain.request.InviteVideoCallInviteRequest;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : VideoCallInviteEvent
 * @date : 2023/4/19 20:39
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class VideoCallInviteEvent extends AbstractEvent<InviteVideoCallInviteRequest> {

    public VideoCallInviteEvent(Object source) {
        super(source);
    }


    public VideoCallInviteEvent(Object source, InviteVideoCallInviteRequest data) {
        super(source);
        this.data = data;
    }

}
