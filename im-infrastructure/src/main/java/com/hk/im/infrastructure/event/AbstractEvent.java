package com.hk.im.infrastructure.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author : HK意境
 * @ClassName : AbstractEvent
 * @date : 2023/1/1 19:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
@Setter
public class AbstractEvent<T> extends ApplicationEvent {

    protected String message;
    protected int code;
    protected T data;

    public AbstractEvent(Object source) {
        super(source);
    }


}



