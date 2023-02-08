package com.hk.im.infrastructure.event.friend.event;

import com.hk.im.domain.entity.FriendApply;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : AddFriendSuccessEvent
 * @date : 2023/2/8 22:39
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class AddFriendSuccessEvent extends AbstractEvent<FriendApply> {
    public AddFriendSuccessEvent(Object source) {
        super(source);
    }

    public AddFriendSuccessEvent(Object source, FriendApply friendApply) {
        super(source);
        this.data = friendApply;
    }
}