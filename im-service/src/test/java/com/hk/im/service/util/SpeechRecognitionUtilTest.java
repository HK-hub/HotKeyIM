package com.hk.im.service.util;

import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : SpeechRecognitionUtilTest
 * @date : 2023/3/18 16:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class SpeechRecognitionUtilTest {

    private String secretId = "";
    private String secretKey = "";
    @Test
    void createRecTask() {
    }


    /**
     * 60 秒内语音识别
     */
    @Test
    void sentenceRecognition() {

        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SpeechRecognitionUtil client = new SpeechRecognitionUtil(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SentenceRecognitionRequest req = new SentenceRecognitionRequest();
            req.setProjectId(1L);
            req.setSubServiceType(2L);
            req.setEngSerViceType("16k_zh_dialect");
            req.setSourceType(0L);
            req.setVoiceFormat("wav");
            req.setUrl("http://47.108.146.141:9000/file//upload/50021d035b7ccf66fe9b863ee9cbe570.wav");
            req.setUsrAudioKey("im");
            // 返回的resp是一个SentenceRecognitionResponse的实例，与请求对象对应
            SentenceRecognitionResponse resp = client.sentenceRecognition(req);
            // 输出json格式的字符串回包
            System.out.println(SentenceRecognitionResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

}