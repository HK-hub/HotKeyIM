package com.hk.im.server.chat;

import com.hk.im.server.chat.server.HotKeyChatServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : ChatServerBootStarter
 * @date : 2023/1/4 21:04
 * @description : netty 服务器引导者
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class ChatServerBootStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("IOC 容器准备完毕,准备创建Chat Server: {}", LocalDateTime.now());
        ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
        if (Objects.isNull(parent)) {
            log.info("启动Chat Server: {}-{}", this.getClass(), LocalDateTime.now());
            // hotKeyChatServer.runServer();
            new Thread(() -> {
                HotKeyChatServer.getInstance().runServer();
            }).start();
        }
    }
}
