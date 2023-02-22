package com.hk.im.domain.constant;

import com.hk.im.domain.message.WebSocketMessage;
import com.hk.im.domain.message.control.ConnectMessage;
import com.hk.im.domain.message.system.NoSupportMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : MessageConstants
 * @date : 2023/1/5 13:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageConstants {

    public static final Map<Integer, Class<? extends WebSocketMessage>> MESSAGE_CLASSES_MAP = new HashMap<>();
    // 默认一页聊天记录数
    public static final Integer DEFAULT_RECORDS_LIMIT = 30;

    public static enum MessageActionType {

        // 连接初始化消息
        CONNECT,
        // 普通聊天消息
        SIMPLE_CHAT,
        // 好友上下线状态
        ONLINE_STATUS,
        // 好友申请消息
        FRIEND_APPLY,
        // 群申请消息
        GROUP_APPLY,
        // 加入群聊消息
        GROUP_JOIN,
        // 退出群聊消息
        GROUP_QUIT,
        // 非法操作消息
        NO_SUPPORT,

    }

    static {
        MESSAGE_CLASSES_MAP.put(MessageActionType.CONNECT.ordinal(), ConnectMessage.class);
        MESSAGE_CLASSES_MAP.put(MessageActionType.NO_SUPPORT.ordinal(), NoSupportMessage.class);

    }

}
