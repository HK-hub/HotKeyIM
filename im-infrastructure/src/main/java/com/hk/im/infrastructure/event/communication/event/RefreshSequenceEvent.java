package com.hk.im.infrastructure.event.communication.event;

import com.hk.im.domain.entity.Sequence;
import com.hk.im.infrastructure.event.AbstractEvent;
import org.springframework.context.ApplicationEvent;

/**
 * @author : HK意境
 * @ClassName : RefreshSequenceEvent
 * @date : 2023/1/26 21:29
 * @description : 刷新Sequence 事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class RefreshSequenceEvent extends AbstractEvent<Sequence> {

    public RefreshSequenceEvent(Object source) {
        super(source);
    }

    public RefreshSequenceEvent(Object source, Sequence sequence) {
        super(source);
        this.data = sequence;
    }
}
