package com.hk.im.infrastructure.event.user.event;

import com.hk.im.domain.entity.User;
import com.hk.im.infrastructure.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : UserRegisterEvent
 * @date : 2023/2/8 21:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserRegisterEvent extends AbstractEvent<User> {


    public UserRegisterEvent(Object source) {
        super(source);
    }

    public UserRegisterEvent(Object source, User user){
        this(source);
        this.data = user;
    }

}
