package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.AudioConvertRequest;

/**
 * @author : HK意境
 * @ClassName : SpeechRecognitionService
 * @date : 2023/3/18 16:52
 * @description : 语音识别
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface SpeechRecognitionService {

    public ResponseResult shortSentenceRecognition(AudioConvertRequest request);

}
