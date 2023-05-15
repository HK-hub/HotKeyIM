package com.hk.im.flow.data.cdc.event.events.friend;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : FriendEvent
 * @date : 2023/5/15 19:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class FriendEvent extends AbstractEvent<JSONObject> {
    public FriendEvent(Object source) {
        super(source);
    }

    public FriendEvent(Object source, JSONObject data) {
        super(source);
        this.data  = data;
    }



}
