package com.hk.im.flow.data.cdc.event.events.note;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.AbstractEvent;

/**
 * @author : HK意境
 * @ClassName : NoteEvent
 * @date : 2023/5/15 19:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class NoteEvent extends AbstractEvent<JSONObject> {

    public NoteEvent(Object source) {
        super(source);
    }

    public NoteEvent(Object source, JSONObject jsonObject) {
        super(source);
        this.data = jsonObject;
    }

}
