package com.hk.im.server.common.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : HK意境
 * @ClassName : OutboundDataEvent
 * @date : 2023/4/19 21:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class OutboundDataEvent {

    /**
     * 流入数据类型,事件类型
     */
    @Getter
    public static enum OutboundEventTypeEnum {

        DEFAULT_EVENT("default-event"),
        SIGNALING_EVENT("signaling-event"),
        HEAR_BEAT("heartbeat"),
        EVENT_TALK_READ("event_talk_read")

        ,;
        private String event;

        OutboundEventTypeEnum(String event) {
            this.event = event;
        }
    }


    /**
     * 获取事件类型对应的枚举
     * @param event
     * @return
     */
    public static OutboundDataEvent.OutboundEventTypeEnum getEventType(String event) {
        for (OutboundDataEvent.OutboundEventTypeEnum value : OutboundDataEvent.OutboundEventTypeEnum.values()) {
            if (StringUtils.equals(value.getEvent(), event)) {
                // 匹配事件
                return value;
            }
        }
        // 默认事件Resp
        return OutboundDataEvent.OutboundEventTypeEnum.DEFAULT_EVENT;
    }

}
