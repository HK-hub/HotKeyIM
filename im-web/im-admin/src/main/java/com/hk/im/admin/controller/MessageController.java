package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.TalkRecordsRequest;
import com.hk.im.service.service.ChatMessageService;
import com.hk.im.service.service.MessageFlowService;
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



}
