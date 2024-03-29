package com.hk.im.admin.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.hk.flow.sentinel.handler.SendMessageBlockedHandler;
import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.MessageFlowService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.message.chat.AttachmentMessage;
import com.hk.im.domain.message.chat.ImageMessage;
import com.hk.im.domain.message.chat.TextMessage;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.message.DeleteRecordsRequest;
import com.hk.im.domain.request.message.RevokeMessageRequest;
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
    @PostMapping("/records/history")
    public ResponseResult getCommunicationRecords(@RequestBody TalkRecordsRequest request) {

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
     * 撤回消息
     * @param request
     * @return
     */
    @PostMapping("/revoke/message")
    public ResponseResult revokeMessage(@RequestBody RevokeMessageRequest request) {

        return this.messageFlowService.revokeMessage(request);
    }

    /**
     * 删除聊天消息
     * @param request
     * @return
     */
    @PostMapping("/delete/message")
    public ResponseResult deleteMessageRecords(@RequestBody DeleteRecordsRequest request) {

        return this.messageFlowService.deleteMessageRecords(request);
    }

    /**
     * 发送普通文本消息
     * @param message
     * @return
     */
    @SentinelResource(value = "sendTextMessage", blockHandler = "handleTextBlock", blockHandlerClass = {SendMessageBlockedHandler.class})
    @PostMapping("/send/text")
    public ResponseResult sendTextMessage(@RequestBody TextMessage message) {

        return this.messageFlowService.sendTextMessage(message);
    }


    /**
     * 发送图片消息
     * @param request
     * @return {@link ImageMessage}
     */
    @SentinelResource(value = "sendImageMessage", blockHandler = "handleImageBlock", blockHandlerClass = {SendMessageBlockedHandler.class})
    @PostMapping("/send/image")
    public ResponseResult sendImageMessage(ImageMessage request) {

        return this.messageFlowService.sendImageMessage(request);
    }


    /**
     * 发送附件，文件消息
     * @param request
     * @return
     */
    @SentinelResource(value = "sendFileMessage", blockHandler = "handleFileBlock", blockHandlerClass = {SendMessageBlockedHandler.class})
    @PostMapping("/send/file")
    public ResponseResult sendAttachmentMessage(@RequestBody AttachmentMessage request) {

        return this.messageFlowService.sendAttachmentMessage(request);
    }


    /**
     * 发送代码消息
     * @param request
     * @return
     */
    @SentinelResource(value = "sendCodeMessage", blockHandler = "handleCodeBlock", blockHandlerClass = {SendMessageBlockedHandler.class})
    @PostMapping("/send/code")
    public ResponseResult sendCodeMessage(@RequestBody CodeMessageRequest request) {
        return this.messageFlowService.sendCodeMessage(request);
    }


    /**
     * 发起视频通话
     * @param request
     * @return
     */
    @PostMapping("/send/video")
    public ResponseResult sendVideoMessage(@RequestBody InviteVideoCallRequest request) {

        return this.messageFlowService.sendVideoMessage(request);
    }


    /**
     * 发送位置消息
     * @param request
     * @return
     */
    @PostMapping("/send/location")
    public ResponseResult sendLocationMessage(@RequestBody LocationMessageRequest request) {

        return this.messageFlowService.sendLocationMessage(request);
    }


    /**
     * 发起视频通话邀请
     * @param request
     * @return
     */
    @PostMapping("/send/video/call/invite")
    public ResponseResult sendVideoCallInviteMessage(@RequestBody InviteVideoCallInviteRequest request) {

        return this.messageFlowService.sendVideoInviteMessage(request);
    }

}
