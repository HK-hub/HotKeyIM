package com.hk.im.admin.config;

import com.hk.im.service.util.SpeechRecognitionUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : HK意境
 * @ClassName : TencentApiConfig
 * @date : 2023/3/18 17:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class TencentApiConfig {

    @Value("${api.tencent.secretId}")
    private String secretId;
    @Value("${api.tencent.secretKey}")
    private String secretKey;
    @Value("${api.tencent.audio.endpoint}")
    private String endpoint;

    @Bean
    public SpeechRecognitionUtil speechRecognitionUtil() {
        Credential credential = new Credential(secretId, secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(endpoint);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SpeechRecognitionUtil(credential, "", clientProfile);
    }


}
