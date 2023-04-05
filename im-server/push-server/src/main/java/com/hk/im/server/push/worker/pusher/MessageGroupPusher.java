package com.hk.im.server.push.worker.pusher;

import com.hk.im.domain.vo.MessageVO;
import com.hk.im.server.push.worker.MessageOfflineWorker;
import com.hk.im.server.push.worker.MessageSynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


    @Resource
    private MessageSynchronizer messageSynchronizer;
    @Resource
    private MessageOfflineWorker messageOfflineWorker;


    /**
     * 群聊推送消息
     * @param messageVO
     */
    public void pushMessage(MessageVO messageVO) {

        // 将消息推送给在线用户
        messageSynchronizer.synchronizeGroup(messageVO);

        // 离线消息处理
        messageOfflineWorker.processOfflineMessage(messageVO);
    }
}
