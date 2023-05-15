package com.hk.im.flow.data.cdc.event.listener;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.events.friend.FriendEvent;
import com.hk.im.flow.data.cdc.event.events.group.MemberEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MessageEventListener
 * @date : 2023/5/15 19:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MemberEventListener {


    /**
     * 消息创建
     * @param memberEvent
     */
    @Async
    @EventListener
    public void onEvent(MemberEvent memberEvent) {
        log.info("onEvent: {}", memberEvent);
        JSONObject jsonObject = memberEvent.getData();
    }

}
