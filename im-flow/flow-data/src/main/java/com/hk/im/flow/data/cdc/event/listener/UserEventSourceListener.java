package com.hk.im.flow.data.cdc.event.listener;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.flow.data.cdc.event.events.user.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MessageEventSourceListener
 * @date : 2023/5/15 19:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class UserEventSourceListener {


    /**
     * 消息创建
     * @param userEvent
     */
    @Async
    @EventListener
    public void onEvent(UserEvent userEvent) {

        JSONObject jsonObject = userEvent.getData();


    }

}
