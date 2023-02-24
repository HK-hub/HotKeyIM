package com.hk.im.server.push.comsumer;

import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.MessageConstants;
import com.hk.im.domain.constant.MessageQueueConstants;
import com.hk.im.domain.message.control.ConnectMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : UserConnectedConsumer
 * @date : 2023/1/5 21:41
 * @description : 用户上线消息消费，需要进行消息推送了
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.producer.group}", topic = MessageQueueConstants.CONNECT_MESSAGE_TAG)
public class UserConnectedConsumer implements RocketMQListener<ConnectMessage> {

    @Resource
    private UserService userService;

    /**
     * 消费用户上线消息
     * @param connectMessage
     */
    @Override
    public void onMessage(ConnectMessage connectMessage) {
        log.info("rocketMQ onMessage: {}",connectMessage);
        ResponseResult userAndInfo = userService.getUserAndInfo(connectMessage.getUserId());
        log.info("connected user:{}",userAndInfo.getData());

    }
}
