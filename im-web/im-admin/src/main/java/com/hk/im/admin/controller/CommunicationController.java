package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.CreateCommunicationRequest;
import com.hk.im.service.service.ChatCommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : CommunicationController
 * @date : 2023/1/11 18:11
 * @description : 会话
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/talk")
public class CommunicationController {

    @Resource
    private ChatCommunicationService chatCommunicationService;


    /**
     * 获取用户会话列表
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getCommunicationList(@RequestParam("userId") String userId) {
        log.info("getCommunicationList, request user={}", userId);
        return this.chatCommunicationService.getUserCommunicationList(Long.valueOf(userId));
    }


    /**
     * 创建会话: 如果会话存在，则直接返回会话
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createCommunication(@RequestBody CreateCommunicationRequest request) {

        return this.chatCommunicationService.createChatCommunication(request);
    }


}
