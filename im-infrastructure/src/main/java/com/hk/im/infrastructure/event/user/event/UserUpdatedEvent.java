package com.hk.im.infrastructure.event.user.event;

import com.hk.im.infrastructure.event.AbstractEvent;
import com.hk.im.domain.entity.User;

/**
 * @author : HK意境
 * @ClassName : UserUpdatedEvent
 * @date : 2023/1/1 20:25
 * @description : 用户更新事件：1.上传头像
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserUpdatedEvent extends AbstractEvent<User> {
    public UserUpdatedEvent(Object source) {
        super(source);
    }

    public UserUpdatedEvent(Object source, User user) {
        this(source);
        this.data = user;
    }

}
