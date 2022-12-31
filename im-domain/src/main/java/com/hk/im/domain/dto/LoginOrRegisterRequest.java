package com.hk.im.domain.dto;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : LoginOrRegisterRequest
 * @date : 2022/12/30 17:02
 * @description : 登录类型：账号密码类型，验证码类型：邮箱验证码，短信验证码，第三方登录类型
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class LoginOrRegisterRequest {

    // 登录类型
    private Integer type;
    // 账号:本家账号，或第三方账号
    private String account;
    // 密码: 密码复杂度校验
    private String password;
    // 昵称：用于注册
    private String nickname;
    // 手机号
    private String phone;
    // 验证码
    private String code;
    // 邮箱
    private String email;


    // 登录注册类型
    public static enum LoginType {

        // 账号(手机，邮箱)-密码
        PASSWORD,
        // 验证码登录
        CODE,
        // 第三方
        THIRD
        ;

    }

}
