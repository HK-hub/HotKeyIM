package com.hk.im.flow.data.cdc.event.events.group;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : MemberEvent
 * @date : 2023/5/15 19:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MemberEvent extends AbstractEvent<JSONObject> {
    public MemberEvent(Object source) {
        super(source);
    }

    public MemberEvent(Object source, JSONObject data) {
        super(source);
        this.data = data;
    }

}
