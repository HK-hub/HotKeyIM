package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_user_info
 */
@TableName(value ="tb_user_info")
@Data
@Accessors(chain = true)
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
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 用户个人名片背景
     */
    @TableField(value = "background")
    private String background;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
            && (this.getWechat() == null ? other.getWechat() == null : this.getWechat().equals(other.getWechat()))
            && (this.getGithub() == null ? other.getGithub() == null : this.getGithub().equals(other.getGithub()))
            && (this.getDingtalk() == null ? other.getDingtalk() == null : this.getDingtalk().equals(other.getDingtalk()))
            && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
            && (this.getWallet() == null ? other.getWallet() == null : this.getWallet().equals(other.getWallet()))
            && (this.getLastTime() == null ? other.getLastTime() == null : this.getLastTime().equals(other.getLastTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getConstellation() == null ? other.getConstellation() == null : this.getConstellation().equals(other.getConstellation()))
            && (this.getCampus() == null ? other.getCampus() == null : this.getCampus().equals(other.getCampus()))
            && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()))
            && (this.getJob() == null ? other.getJob() == null : this.getJob().equals(other.getJob()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getInterest() == null ? other.getInterest() == null : this.getInterest().equals(other.getInterest()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getSignature() == null ? other.getSignature() == null : this.getSignature().equals(other.getSignature()))
            && (this.getAddFriendPolicy() == null ? other.getAddFriendPolicy() == null : this.getAddFriendPolicy().equals(other.getAddFriendPolicy()))
            && (this.getAddFriendQuestion() == null ? other.getAddFriendQuestion() == null : this.getAddFriendQuestion().equals(other.getAddFriendQuestion()))
            && (this.getAddFriendAnswer() == null ? other.getAddFriendAnswer() == null : this.getAddFriendAnswer().equals(other.getAddFriendAnswer()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getBackground() == null ? other.getBackground() == null : this.getBackground().equals(other.getBackground()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getWechat() == null) ? 0 : getWechat().hashCode());
        result = prime * result + ((getGithub() == null) ? 0 : getGithub().hashCode());
        result = prime * result + ((getDingtalk() == null) ? 0 : getDingtalk().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getWallet() == null) ? 0 : getWallet().hashCode());
        result = prime * result + ((getLastTime() == null) ? 0 : getLastTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getConstellation() == null) ? 0 : getConstellation().hashCode());
        result = prime * result + ((getCampus() == null) ? 0 : getCampus().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        result = prime * result + ((getJob() == null) ? 0 : getJob().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getInterest() == null) ? 0 : getInterest().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getSignature() == null) ? 0 : getSignature().hashCode());
        result = prime * result + ((getAddFriendPolicy() == null) ? 0 : getAddFriendPolicy().hashCode());
        result = prime * result + ((getAddFriendQuestion() == null) ? 0 : getAddFriendQuestion().hashCode());
        result = prime * result + ((getAddFriendAnswer() == null) ? 0 : getAddFriendAnswer().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getBackground() == null) ? 0 : getBackground().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", nickname=").append(nickname);
        sb.append(", qq=").append(qq);
        sb.append(", wechat=").append(wechat);
        sb.append(", github=").append(github);
        sb.append(", dingtalk=").append(dingtalk);
        sb.append(", openid=").append(openid);
        sb.append(", wallet=").append(wallet);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", status=").append(status);
        sb.append(", gender=").append(gender);
        sb.append(", age=").append(age);
        sb.append(", birthday=").append(birthday);
        sb.append(", constellation=").append(constellation);
        sb.append(", campus=").append(campus);
        sb.append(", major=").append(major);
        sb.append(", job=").append(job);
        sb.append(", city=").append(city);
        sb.append(", interest=").append(interest);
        sb.append(", tag=").append(tag);
        sb.append(", signature=").append(signature);
        sb.append(", addFriendPolicy=").append(addFriendPolicy);
        sb.append(", addFriendQuestion=").append(addFriendQuestion);
        sb.append(", addFriendAnswer=").append(addFriendAnswer);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", background=").append(background);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}