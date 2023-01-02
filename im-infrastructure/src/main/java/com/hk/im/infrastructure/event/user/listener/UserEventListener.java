package com.hk.im.infrastructure.event.user.listener;

import com.hk.im.infrastructure.event.user.event.UserUpdatedEvent;
import com.hk.im.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : UserEventListener
 * @date : 2023/1/1 20:21
 * @description : 监听用户事件
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class UserEventListener {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户更新事件
     * @param event
     */
    @Async
    @EventListener
    public void userUpdatedEventHandler(UserUpdatedEvent event) {

        // 更新用户信息
        this.userMapper.updateById(event.getData());
    }


}
