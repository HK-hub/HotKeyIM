package com.hk.im.infrastructure.event.friend.listener;

import com.hk.im.infrastructure.event.friend.event.FriendApplyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : FriendEventListener
 * @date : 2023/1/2 19:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class FriendEventListener {


    /**
     * 添加好友事件
     * @param event
     */
    @EventListener
    public void FriendApplyListener(FriendApplyEvent event) {
        // 写通知
        log.info("好友申请事件：{}", event.getData());

    }


}
