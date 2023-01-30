package com.hk.im.infrastructure.event.communication.listener;

import com.hk.im.domain.entity.Sequence;
import com.hk.im.infrastructure.event.communication.event.RefreshSequenceEvent;
import com.hk.im.infrastructure.mapper.SequenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
    @EventListener
    public void handleRefreshSequenceEvent(RefreshSequenceEvent event) {

        // 获取剩余率
        Sequence sequence = event.getData();
        Long current = sequence.getCurrent();
        Long max = sequence.getMax();
        Integer segment = sequence.getSegment();

        // 如果 current > max
        if (current > max) {
            sequenceMapper.updateById(sequence.setCurrent(current).setMax(current + segment));
        }

        // 剩余率
        double rate = 1.0 * (max - current) / segment;
        if (rate <= 0.25) {
            // 刷新
            sequenceMapper.updateById(sequence.setCurrent(current).setMax(current + segment));
        }
    }

}
