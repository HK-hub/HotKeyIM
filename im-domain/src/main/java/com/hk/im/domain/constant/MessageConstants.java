package com.hk.im.domain.constant;

import com.hk.im.domain.message.WebSocketMessage;
import com.hk.im.domain.message.control.ConnectMessage;
import com.hk.im.domain.message.system.NoSupportMessage;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public static final String CONSUMER_GROUP = "hkim-producer-group";
    //
    public static final String CHAT_MESSAGE_TAG = "chat-message-tag";


    // 消息动作类型
    public static enum MessageActionType {

        // 连接初始化消息
        CONNECT,
        // 普通聊天消息
        CHAT,
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

    // 消息事件
    @Getter
    public enum MessageEventType {
        // 连接初始化消息
        CONNECT("connect"),
        // 普通聊天消息
        CHAT("event_talk"),
        ;

        private String event;

        MessageEventType(String event) {
            this.event = event;
        }
    }


    // 聊天消息类型
    public static enum ChatMessageType {

        // 默认
        DEFAULT,
        // 普通文本消息
        TEXT,
        // 图片消息
        IMAGE,
        // 代码消息
        CODE,
        // 语音消息,视频消息
        AUDIO,
        // 视频消息
        VIDEO,
        // 文件消息
        FILE,
        // 红包消息: 在紅包裡裝錢的用意就是要祝福收到的人在新的一年中平安吉祥、好運連連，所以英文就直接稱之為 lucky money
        MONEY,
        // 链接消息: link
        LINK,
        // 卡片消息
        CARD,
        // 位置消息
        LOCATION,
        // 转发消息
        FORWARD,
        // 语音通话
        VOICE_CALL,
        // 视频通话
        VIDEO_CALL,
        // 分享屏幕
        SCREEN_SHARE,
        // 白板演示
        WHITEBOARD,
        // 屏幕控制
        SCREEN_CONTROL,
    }


    // 消息特性

    /**
     * 消息属性：0.默认，1.离线消息，2.漫游消息，3.同步消息，4.透传消息，5.控制消息
     */
    public enum MessageFeature {

        DEFAULT,
        OFFLINE,
        ROAMING,
        SYNC,
        TRANSPARENT,
        CONTROL,

    }


    static {
        MESSAGE_CLASSES_MAP.put(MessageActionType.CONNECT.ordinal(), ConnectMessage.class);
        MESSAGE_CLASSES_MAP.put(MessageActionType.NO_SUPPORT.ordinal(), NoSupportMessage.class);

    }


    /**
     * 消息签收状态：签收状态：1.未读，2.已读，3.忽略，4.撤回，5.删除
     */
    public enum SignStatsEnum {

        UNKOWN,
        UNREAD,
        READ,
        IGNORE,
        REVOKE,
        DELETED,

    }


    // 消息发送状态：1.发送中，2.已发送，3.发送失败,4.草稿，
    public enum SendStatusEnum {

        UNKOWN,
        SENDING,
        SENDED,
        FAIL,
        DRAFT,

    }

    public static enum FileSubType {

        DEFAULT,
        IMAGE,
        VOICE,
        VIDEO,
        COMPRESS,
        TEXT,
        CODE,
        FILE,
    }


    /**
     * 支持文件预览的文件类型
     */
    public static Set<String> enablePreviewFileType = Set.of("txt", "pdf", "docx", "doc", "ppt", "zip", "pptx", "xls", "xlsx",
            "htm", "asp", "jsp", "xml", "json", "properties", "md", "gitignore", "log", "bat", "m", "bas", "prg", "cmd",
            "cpp", "java", "c", "h", "go", "py", "kt", "js", "css", "csv", "rust", "sh", "sql","mp3","wav","mp4","flv");

}
