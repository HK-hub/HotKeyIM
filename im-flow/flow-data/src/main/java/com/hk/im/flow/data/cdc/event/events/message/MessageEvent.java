package com.hk.im.flow.data.cdc.event.events.message;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : MessageEvent
 * @date : 2023/5/15 19:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageEvent extends AbstractEvent<JSONObject> {
    public MessageEvent(Object source) {
        super(source);
    }

    public MessageEvent(Object source, JSONObject data) {
        super(source);
        this.data = data;
    }

}
