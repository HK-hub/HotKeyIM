package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_user_info
 */
@TableName(value ="tb_user_info")
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class UserInfo implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 用户昵称，同用户名
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 绑定的QQ号
     */
    @TableField(value = "qq")
    private String qq;

    /**
     * 绑定的微信号
     */
    @TableField(value = "wechat")
    private String wechat;

    /**
     * github 账号
     */
    @TableField(value = "github")
    private String github;

    /**
     * 钉钉账号
     */
    @TableField(value = "dingtalk")
    private String dingtalk;

    /**
     * openid 可用于微信登录
     */
    @TableField(value = "openid")
    private String openid;

    /**
     * 用户钱包剩余余额
     */
    @TableField(value = "wallet")
    private Integer wallet;

    /**
     * 最后交互时间
     */
    @TableField(value = "last_time")
    private LocalDateTime lastTime;

    /**
     * 用户状态：1.离线，2.在线，3.隐身,4.挂起，5.忙碌
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 性别: 1.男，0.女
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 生日
     */
    @TableField(value = "birthday")
    private LocalDate birthday;

    /**
     * 星座
     */
    @TableField(value = "constellation")
    private String constellation;

    /**
     * 学校
     */
    @TableField(value = "campus")
    private String campus;

    /**
     * 专业,主修
     */
    @TableField(value = "major")
    private String major;

    /**
     * 职业
     */
    @TableField(value = "job")
    private String job;

    /**
     * 所在城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 兴趣爱好
     */
    @TableField(value = "interest")
    private String interest;

    /**
     * 个人标签,不超过6个标签，每个标签不超过6个字
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 个性签名，类比QQ签名
     */
    @TableField(value = "signature")
    private String signature;

    /**
     * 加好友策略：0.直接同意，1.验证，2.回答问题，3.输入密码
     */
    @TableField(value = "add_friend_policy")
    private Integer addFriendPolicy;

    /**
     * 加好友问题
     */
    @TableField(value = "add_friend_question")
    private String addFriendQuestion;

    /**
     * 加好友答案，密码
     */
    @TableField(value = "add_friend_answer")
    private String addFriendAnswer;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}