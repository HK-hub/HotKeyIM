package com.hk.im.domain.vo;

import com.hk.im.common.consntant.RedisConstants;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author : HK意境
 * @ClassName : UserVO
 * @date : 2022/12/31 12:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString
@Accessors(chain = true)
public class UserVO {

    private Long id;

    // accessToken 凭证
    private String accessToken;

    // 凭证过期时间: 12 小时=43200L 秒
    private Long expiresIn = RedisConstants.ACCESS_TOKEN_TTL;

    private String username;

    private String account;

    // 需要加密
    private String phone;

    private String email;

    private String bigAvatar;

    private String miniAvatar;

    private String qrcode;

    private String qq;

    private String wechat;

    private String github;

    private String dingtalk;

    private Integer wallet;

    /**
     * 用户状态：1.离线，2.在线，3.隐身,4.挂起，5.忙碌
     */
    private Integer status;

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

    private String city;

    private String interest;

    /**
     * 个人标签,不超过6个标签，每个标签不超过6个字
     */
    private String tag;

    /**
     * 个性签名，类比QQ签名
     */
    private String signature;

    /**
     * 好友分组id
     */
    private String groupId;

}
