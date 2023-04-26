package com.hk.im.infrastructure.event.group.event;

import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.domain.request.RemoveGroupMemberRequest;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : LeaveGroupEvent
 * @date : 2023/2/14 12:30
 * @description : 离开群聊事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class LeaveGroupEvent extends AbstractEvent<RemoveGroupMemberRequest> {
    public LeaveGroupEvent(Object source) {
        super(source);
    }

    public LeaveGroupEvent(Object source, RemoveGroupMemberRequest data) {
        super(source);
        this.data = data;
    }
}
