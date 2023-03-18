package com.hk.im.admin.controller;

import com.hk.im.client.service.SpeechRecognitionService;
import com.hk.im.client.service.TalkRecordService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.AudioConvertRequest;
import com.hk.im.domain.request.DownloadMessageFileRequest;
import com.tencent.asr.model.SpeechRecognitionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : TalkRecordController
 * @date : 2023/3/15 22:13
 * @description : 聊天记录控制器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/record")
public class TalkRecordController {

    @Resource
    private TalkRecordService talkRecordService;
    @Resource
    private SpeechRecognitionService speechRecognitionService;

    /**
     * 下载聊天记录文件
     * @param request
     * @return
     */
    @PostMapping("/file/download")
    public ResponseResult downloadMessageFile(@RequestBody DownloadMessageFileRequest request) {
        return this.talkRecordService.downloadRecordFile(request);
    }

    /**
     * 预览聊天记录文件
     * @param request
     * @return
     */
    @PostMapping("/file/preview")
    public ResponseResult previewMessageFile(@RequestBody DownloadMessageFileRequest request) {
        return this.talkRecordService.previewMessageFile(request);
    }

    /**
     * 语音消息语音识别，语音转文本
     * @param request
     * @return
     */
    @PostMapping("/audio/recognition")
    public ResponseResult speechRecognition(@RequestBody AudioConvertRequest request) {

        return this.speechRecognitionService.shortSentenceRecognition(request);
    }


}
