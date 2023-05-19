package com.hk.im.flow.data.cdc.event.listener;

import com.alibaba.fastjson2.JSONObject;
import com.hk.im.domain.entity.MessageFlow;
import com.hk.im.flow.data.cdc.event.events.message.MessageEvent;
import com.hk.im.flow.data.cdc.process.MessageFlowProcessor;
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
public class MessageEventSourceListener {


    @Resource
    private MessageFlowProcessor messageFlowProcessor;

    /**
     * 消息创建
     * @param messageEvent
     */
    @Async
    @EventListener
    public void onEvent(MessageEvent messageEvent) {

        JSONObject jsonObject = messageEvent.getData();

        // 解析消息
        String op = jsonObject.getString("op");
        MessageFlow before = jsonObject.getObject("before", MessageFlow.class);
        MessageFlow after = jsonObject.getObject("after", MessageFlow.class);

        switch (op) {
            case "u":
                this.messageFlowProcessor.update(before, after);
                break;
            case "c":
                this.messageFlowProcessor.create(after);
                break;
            case "d":
                this.messageFlowProcessor.remove(before, after);
                break;
            default:
                break;
        }

    }

}
