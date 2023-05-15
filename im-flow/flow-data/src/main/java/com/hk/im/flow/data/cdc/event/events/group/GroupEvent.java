package com.hk.im.flow.data.cdc.event.events.group;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : GroupEvent
 * @date : 2023/5/15 19:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class GroupEvent extends AbstractEvent<JSONObject> {
    public GroupEvent(Object source) {
        super(source);
    }

    public GroupEvent(Object source, JSONObject data) {
        super(source);
        this.source = data;
    }
}
