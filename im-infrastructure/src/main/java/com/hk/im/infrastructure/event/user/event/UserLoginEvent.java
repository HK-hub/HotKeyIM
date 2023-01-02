package com.hk.im.infrastructure.event.user.event;

import com.hk.im.infrastructure.event.AbstractEvent;
import com.hk.im.domain.entity.User;

/**
 * @author : HK意境
 * @ClassName : UserLoginEvent
 * @date : 2023/1/1 20:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserLoginEvent extends AbstractEvent<User> {
    public UserLoginEvent(Object source) {
        super(source);

    }

    public UserLoginEvent(Object source, User user) {
        this(source);
        this.data = user;
    }

}
