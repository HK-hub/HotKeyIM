package com.hk.im.infrastructure.event.communication.listener;

import com.hk.im.domain.entity.Sequence;
import com.hk.im.infrastructure.event.communication.event.RefreshSequenceEvent;
import com.hk.im.infrastructure.mapper.SequenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


    @Resource
    private SequenceMapper sequenceMapper;

    /**
     * 处理 发号器刷新事件
     * @param event
     */
    @Async
    @EventListener
    public void handleRefreshSequenceEvent(RefreshSequenceEvent event) {

        // 获取剩余率
        Sequence sequence = event.getData();
        log.info("refresh sequence and session ttl: {}",sequence);

    }

}
