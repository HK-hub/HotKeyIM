package com.hk.im.domain.constant;

import com.hk.im.common.consntant.RedisConstants;

/**
 * @author : HK意境
 * @ClassName : CommunicationConstants
 * @date : 2023/2/11 11:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CommunicationConstants {


    // 会话类型
    public static enum SessionType {
        DEFAULT,
        PRIVATE,
        GROUP,
        SYSTEM,
        ROBOT,
    }


    /**
     * 获取 Redis 会话id
     * @param senderId
     * @param receiverId
     * @return
     */
    public static String getCommunicationKey(Long senderId, Long receiverId) {
        String name = senderId < receiverId ? senderId + "-" + receiverId : receiverId + "-" + senderId;
        String key = RedisConstants.COMMUNICATION_KEY + RedisConstants.SEQUENCE_KEY + name;
        return key;
    }
}
