package com.hk.im.server.push.comsumer.control;

import com.hk.im.domain.vo.message.RevokeMessageVO;
import com.hk.im.server.push.worker.control.ControlSynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : RevokeMessageConsumer
 * @date : 2023/5/4 21:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "revoke-message-group", topic = "control-topic",
        selectorExpression = "control-topic", selectorType = SelectorType.TAG)
public class RevokeMessageConsumer implements RocketMQListener<RevokeMessageVO> {

    @Resource
    private ControlSynchronizer controlSynchronizer;


    /**
     * 消费撤回消息
     * @param revokeMessageVO
     */
    @Override
    public void onMessage(RevokeMessageVO revokeMessageVO) {

        log.info("on revoke message consumer: {}", revokeMessageVO);

        // 将撤回消息同步给对方
        try {
            controlSynchronizer.synchronizedRevoke(revokeMessageVO);
        } catch (Exception e) {
            log.error("on revoke message consumer synchronized error:", e);
        }
    }
}
