package com.hk.im.flow.data.cdc.event.events.user;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : UserEvent
 * @date : 2023/5/15 19:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserEvent extends AbstractEvent<JSONObject> {
    public UserEvent(Object source) {
        super(source);
    }

    public UserEvent(Object source, JSONObject data) {
        super(source);
        this.data = data;
    }

}
