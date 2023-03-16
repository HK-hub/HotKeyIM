package com.hk.im.server.common.constants;

/**
 * @author : HK意境
 * @ClassName : MessageTypeConstants
 * @date : 2023/2/28 16:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageTypeConstants {

    public static enum FeatureTypeEnum {

        UNKNOWN,
        CHAT,
        CONNECT,
        CONTROL,
        ;

    }

    public static enum MessageTypeEnum {

        UNKNOWN,
        TEXT,
        IMAGE,
        CODE,

    }

    public static enum ActionTypeEnum {
        UNKNOWN,
        CONNECT,
        CHAT,
        READ,
        SIGN,
    }


}
