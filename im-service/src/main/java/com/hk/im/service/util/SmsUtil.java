package com.hk.im.service.util;

import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.comm.entity.SmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SmsUtil
 * @date : 2023/4/15 17:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class SmsUtil {

    //注入短信工具
    @Resource
    private SmsBlend sms;


    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    public String sendMessage(String phone, String code) {

        SmsResponse smsResponse = sms.sendMessage(phone, code);
        return code;
    }


}
