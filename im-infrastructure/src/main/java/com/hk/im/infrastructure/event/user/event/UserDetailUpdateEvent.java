package com.hk.im.infrastructure.event.user.event;

import com.hk.im.domain.entity.User;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : UserDetailUpdateEvent
 * @date : 2023/3/10 14:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserDetailUpdateEvent extends AbstractEvent<User> {
    public UserDetailUpdateEvent(Object source) {
        super(source);
    }

    public UserDetailUpdateEvent(Object source, User data) {
        super(source);
        this.data = data;
    }


}
