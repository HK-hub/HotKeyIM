package com.hk.im.server.push.worker.pusher;

import com.hk.im.domain.bo.MessageBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MessageGroupPusher
 * @date : 2023/2/28 16:47
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class MessageGroupPusher {

    /**
     * 群聊推送消息
     * @param messageBO
     */
    public void pushMessage(MessageBO messageBO) {
    }
}
