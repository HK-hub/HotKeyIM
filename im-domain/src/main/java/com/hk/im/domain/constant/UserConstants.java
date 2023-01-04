package com.hk.im.domain.constant;

/**
 * @author : HK意境
 * @ClassName : UserConstants
 * @date : 2022/12/30 18:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserConstants {

    public static final String USER_SALT_PREFIX = "user_salt_";
    public static final String LOGIN_OR_REGISTER = "lr";
    public static final String FIND_PASSWORD = "fp";

    // 缩略头像大小
    public static final int MINI_AVATAR_WIDTH = 100;
    public static final int MINI_AVATAR_HEIGHT = 100;

    // 用户二维码
    public static final String USER_QR_CODE = "USER_QR_CODE:";

    // 黑名单
    public static final String BLACK_LIST = "黑名单";


    // 用户状态：1.离线，2.在线，3.隐身,4.挂起，5.忙碌
    public static enum UserStatus {

        DEFAULT,
        // 1.在线
        OFFLINE,
        // 2.离线
        ONLINE,
        // 3.隐身
        UNKNOWN,
        // 4.挂起
        SUSPEND,
        // 5.忙碌
        BUSY,

    }


}
