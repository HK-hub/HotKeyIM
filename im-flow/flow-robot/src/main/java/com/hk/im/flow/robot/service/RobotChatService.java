package com.hk.im.flow.robot.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : RobotChatService
 * @date : 2023/5/6 16:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class RobotChatService {

    @Resource
    private ChatgptService chatgptService;

    public String simpleChatMessage(String message) {
        String responseMessage = this.chatgptService.sendMessage(message);

        log.info("send message:{} to robot and answer is: {}", message, responseMessage);
        return responseMessage;
    }

}
