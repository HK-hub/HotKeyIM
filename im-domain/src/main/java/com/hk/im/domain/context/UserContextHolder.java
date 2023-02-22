package com.hk.im.domain.context;

import com.hk.im.domain.entity.User;

/**
 * @author : HK意境
 * @ClassName : UserContextHolder
 * @date : 2023/1/7 10:23
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserContextHolder {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static User get() {
        return userHolder.get();
    }

    public static Boolean set(User user) {
        userHolder.set(user);
        return true;
    }

    public static Boolean remove() {
        userHolder.remove();
        return true;
    }



}
