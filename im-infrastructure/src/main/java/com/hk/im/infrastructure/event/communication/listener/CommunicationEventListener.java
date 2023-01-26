package com.hk.im.infrastructure.event.communication.listener;

import com.hk.im.infrastructure.event.communication.event.RefreshSequenceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : CommunicationEventListener
 * @date : 2023/1/26 21:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class CommunicationEventListener{


    /**
     * 处理 发号器刷新事件
     * @param event
     */
    @Async
    @EventListener
    public void handleRefreshSequenceEvent(RefreshSequenceEvent event) {

        // 检验

    }

}
