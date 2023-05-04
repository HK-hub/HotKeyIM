package com.hk.im.domain.constant;

import lombok.Getter;

/**
 * @author : HK意境
 * @ClassName : MessageQueueConstants
 * @date : 2023/2/24 15:32
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageQueueConstants {

    // 消费者组
    public static final String CONSUMER_GROUP = "hkim-producer-group";

    // 消息消费组
    public static enum MessageConsumerGroup {

        CONNECT_GROUP("chat-connect-group"),
        CHAT_GROUP("chat-message-group"),
        SIGNALING_GROUP("chat-signal-group")
        ;

        private String group;

        MessageConsumerGroup(String s) {
            this.group = s;
        }
    }


    // 消费者订阅主题
    @Getter
    public static enum MessageConsumerTopic {
        connect_topic("connect-topic"),
        chat_topic("chat-topic"),
        signaling_topic("signaling-topic"),
        control_topic("control-topic")
        ;

        public String topic;
        MessageConsumerTopic(String s) {
            this.topic = s;
        }
    }
    // 消费者订阅主题tag
    @Getter
    public static enum MessageConsumerTag {
        connect_tag("connect-tag"),
        chat_tag("chat-tag"),
        control_tag("control-topic")
        ;

        public String tag;
        MessageConsumerTag(String s) {
            this.tag = s;
        }
    }

}
