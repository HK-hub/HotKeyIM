package com.hk.im.flow.data.cdc.event.listener;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.domain.entity.Friend;
import com.hk.im.flow.data.cdc.event.events.friend.FriendEvent;
import com.hk.im.flow.data.cdc.process.FriendProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
public class FriendEventSourceListener {

    @Resource
    private FriendProcessor friendProcessor;


    /**
     * 消息创建
     * @param friendEvent
     */
    @Async
    @EventListener
    public void onEvent(FriendEvent friendEvent) {
        log.info("onEvent: {}", friendEvent);
        JSONObject jsonObject = friendEvent.getData();

        // 解析消息
        String op = jsonObject.getString("op");
        Friend before = jsonObject.getObject("before", Friend.class);
        Friend after = jsonObject.getObject("after", Friend.class);

        switch (op) {
            case "u":
                this.friendProcessor.update(before, after);
                break;
            case "c":
                this.friendProcessor.create(after);
                break;
            case "d":
                this.friendProcessor.remove(before, after);
                break;
            default:
                break;
        }

    }

}
