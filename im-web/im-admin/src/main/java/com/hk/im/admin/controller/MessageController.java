package com.hk.im.admin.controller;

import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.TalkRecordsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : MessageController
 * @date : 2023/1/11 18:11
 * @description : 消息
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {


    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private MessageFlowService messageFlowService;

    /**
     * 获取历史聊天记录
     * @param request
     * @return
     */
    @GetMapping("/records/history")
    public ResponseResult getCommunicationRecords(TalkRecordsRequest request) {

        return this.messageFlowService.getTalkRecordsByPage(request);
    }


    /**
     * 获取最新的指定数量的聊天记录
     * @param request
     * @return
     */
    @GetMapping("/records/latest")
    public ResponseResult getLastCommunicationRecords(TalkRecordsRequest request) {

        return this.messageFlowService.getLatestTalkRecords(request);
    }


    /**
     * 发送普通文本消息
     * @param message
     * @return
     */
    @PostMapping("/send/text")
    public ResponseResult sendTextMessage(@RequestBody TextMessage message) {

        return this.messageFlowService.sendTextMessage(message);
    }


    /**
     * 发送图片消息
     * @param request
     * @return {@link ImageMessage}
     */
    @PostMapping("/send/image")
    public ResponseResult sendImageMessage(ImageMessage request) {

        return this.messageFlowService.sendImageMessage(request);
    }


    /**
     * 发送附件，文件消息
     * @param request
     * @return
     */
    @PostMapping("/send/file")
    public ResponseResult sendAttachmentMessage(AttachmentMessage request) {

        return this.messageFlowService.sendAttachmentMessage(request);
    }



}
