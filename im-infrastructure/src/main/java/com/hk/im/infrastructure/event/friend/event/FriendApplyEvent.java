package com.hk.im.infrastructure.event.friend.event;

import com.hk.im.domain.entity.FriendApply;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : FriendApplyEvent
 * @date : 2023/1/2 19:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class FriendApplyEvent extends AbstractEvent<FriendApply> {
    public FriendApplyEvent(Object source) {
        super(source);
    }

    public FriendApplyEvent(Object source, FriendApply friendApply) {
        this(source);
        this.data = friendApply;
    }

}
