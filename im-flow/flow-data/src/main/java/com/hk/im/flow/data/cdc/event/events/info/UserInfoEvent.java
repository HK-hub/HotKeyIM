package com.hk.im.flow.data.cdc.event.events.info;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : UserInfoEvent
 * @date : 2023/5/15 19:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserInfoEvent extends AbstractEvent<JSONObject> {
    public UserInfoEvent(Object source) {
        super(source);
    }

    public UserInfoEvent(Object source, JSONObject jsonObject) {
        super(source);
        this.data = jsonObject;
    }


}
