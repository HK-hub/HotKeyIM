package com.hk.im.infrastructure.event.user.event;

import com.hk.im.domain.entity.User;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : UserAvatarUpdateEvent
 * @date : 2023/3/10 14:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserAvatarUpdateEvent extends AbstractEvent<User> {
    public UserAvatarUpdateEvent(Object source) {
        super(source);
    }

    public UserAvatarUpdateEvent(Object source, User user) {
        super(source);
        this.data = user;
    }

}
