package com.hk.im.infrastructure.event.group.listener;

import com.hk.im.infrastructure.event.group.event.JoinGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @author : HK意境
 * @ClassName : GroupEventListener
 * @date : 2023/2/14 12:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class GroupEventListener {

    @Async
    @EventListener
    public void joinGroupEventHandler(JoinGroupEvent event) {

    }


}
