package com.hk.im.domain.vo;

import com.hk.im.common.consntant.RedisConstants;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author : HK意境
 * @ClassName : UserProfile
 * @date : 2023/4/18 21:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UserProfile {

    private Long id;

    private String username;

    private String account;

    private String bigAvatar;

    private String miniAvatar;

    private String qrcode;

    private String qq;

    private String wechat;

    private String github;

    private String dingtalk;

    private Integer gender;

    private Integer age;

    private LocalDate birthday;

    /**
     * 星座
     */
    private String constellation;

    private String campus;

    private String major;

    private String job;

    private String interest;

    /**
     * 个人标签,不超过6个标签，每个标签不超过6个字
     */
    private String tag;

    /**
     * 个性签名，类比QQ签名
     */
    private String signature;


}
