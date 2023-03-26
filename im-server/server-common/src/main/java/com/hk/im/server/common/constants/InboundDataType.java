package com.hk.im.server.common.constants;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : HK意境
 * @ClassName : InboundDataType
 * @date : 2023/3/23 9:55
 * @description : 服务端接收的数据
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class InboundDataType {

    /**
     * 流入数据类型,事件类型
     */
    @Getter
    public static enum InboundEventTypeEnum {

        DEFAULT_EVENT("default-event"),
        SIGNALING_EVENT("signaling-event")

        ,;
        private String event;

        InboundEventTypeEnum(String event) {
            this.event = event;
        }
    }


    /**
     * 获取事件类型对应的枚举
     * @param event
     * @return
     */
    public static InboundEventTypeEnum getEventType(String event) {
        for (InboundEventTypeEnum value : InboundEventTypeEnum.values()) {
            if (StringUtils.equals(value.getEvent(), event)) {
                // 匹配事件
                return value;
            }
        }
        // 默认事件
        return InboundEventTypeEnum.DEFAULT_EVENT;
    }



}
