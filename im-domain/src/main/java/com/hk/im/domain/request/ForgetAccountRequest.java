package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : ForgetAccountRequest
 * @date : 2023/1/6 20:27
 * @description : 找回账号，忘记密码
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class ForgetAccountRequest {

    // 类型
    private Integer type;
    // 账户：邮箱，手机号, 账号
    private String account;
    // 原始密码
    private String oldPassword;
    // 新密码
    private String newPassword;
    // 验证码
    private String code;

    public static enum ActionTypeEnum {

        // 忘记密码
        FORGET,
        // 修改密码
        MODIFY,
    }

}
