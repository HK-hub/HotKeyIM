package com.hk.im.server.common.message;

import com.hk.im.server.common.event.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : MessageTypeDeterminer
 * @date : 2023/2/28 18:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageTypeDeterminer {


    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends AbstractMessage> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }


    private static final Map<Integer, Class<? extends AbstractMessage>> messageClasses = new HashMap<>();

    // 消息类型
    public static Integer PingMessageType = 999;
    public static Integer PongMessageType = 996;
    public static Integer TextMessageType = 1;
    public static Integer ImageMessageType = 2;
    public static Integer CodeMessageType = 3;
    public static Integer FileMessageType = 6;
    public static Integer LocationMessageType = 10;

    static {
        // messageClasses.put(PingMessageType, PingMessage.class);
        // messageClasses.put(PongMessageType, PongMessage.class);
        messageClasses.put(TextMessageType, SimpleTextMessage.class);
        messageClasses.put(ImageMessageType, SimpleImageMessage.class);
        messageClasses.put(FileMessageType, SimpleFileMessage.class);
        messageClasses.put(CodeMessageType, SimpleCodeMessage.class);
        messageClasses.put(LocationMessageType, SimpleLocationMessage.class);
    }




    // 事件类型
    @Getter
    public static enum EventTypeEnum {

        UNKNOWN("unkown_event"),
        CONNECTED("connected_event"),
        HEARTBEAT("heartbeat_event"),
        TALK_EVENT("talk_event"),
        ;

        private String event;

        EventTypeEnum(String event) {
            this.event = event;
        }
    }


}
