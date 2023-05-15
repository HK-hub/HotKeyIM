package com.hk.im.flow.data.cdc.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

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
