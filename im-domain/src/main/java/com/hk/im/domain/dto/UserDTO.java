package com.hk.im.domain.dto;

import com.hk.im.domain.entity.UserInfo;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : UserDTO
 * @date : 2022/12/30 17:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserDTO {

    private Long id;

    private String token;

    private String username;

    private String account;

    private String phone;

    private String email;

    private String bigAvatar;

    private String miniAvatar;

    private String qrcode;

    private String cid;

    private UserInfo userInfo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
