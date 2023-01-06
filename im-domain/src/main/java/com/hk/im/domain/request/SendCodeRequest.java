package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : SendCodeRequest
 * @date : 2023/1/6 17:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class SendCodeRequest {

    private String account;
    // 验证码用途：1.登录注册，2.找回密码，3.修改密码
    private String type;
}
