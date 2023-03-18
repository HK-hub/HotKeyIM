package com.hk.im.service.service;

import com.hk.im.client.service.ChatMessageService;
import com.hk.im.client.service.SpeechRecognitionService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.ChatMessage;
import com.hk.im.domain.request.AudioConvertRequest;
import com.hk.im.service.util.SpeechRecognitionUtil;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : SpeechRecognitionServiceImpl
 * @date : 2023/3/18 17:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {

    @Resource
    private SpeechRecognitionUtil speechRecognitionUtil;
    @Resource
    private ChatMessageService chatMessageService;

    /**
     * 60 秒内语音识别
     * @param request
     * @return
     */
    @Override
    public ResponseResult shortSentenceRecognition(AudioConvertRequest request) {

        // 参数校验
        boolean preChek = Objects.isNull(request) || Objects.isNull(request.getRecordId()) || StringUtils.isEmpty(request.getToken());
        if (BooleanUtils.isTrue(preChek)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 获取聊天消息
        ChatMessage message = this.chatMessageService.getById(request.getRecordId());
        if (Objects.isNull(message)) {
            // 消息不存在
            return ResponseResult.FAIL("语音消息不存在!");
        }

        // 进行语音识别
        SentenceRecognitionRequest req = new SentenceRecognitionRequest();
        req.setProjectId(1L);
        req.setSubServiceType(2L);
        req.setEngSerViceType("16k_zh_dialect");
        req.setSourceType(0L);
        req.setVoiceFormat(FilenameUtils.getExtension(message.getUrl()));
        req.setUrl(message.getUrl());
        req.setUsrAudioKey("im");
        try {
            SentenceRecognitionResponse response = this.speechRecognitionUtil.sentenceRecognition(req);
            return ResponseResult.SUCCESS(response.getResult());
        } catch (TencentCloudSDKException e) {
            log.error("audio message convert to text fail:{}", req, e);
            return ResponseResult.FAIL("语音识别失败!");
        }
    }
}
