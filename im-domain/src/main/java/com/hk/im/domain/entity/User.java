package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名，昵称
     */
    @TableField(value = "username")
    private String username;

    /**
     * 账号，类比QQ号,唯一性
     */
    @TableField(value = "account")
    private String account;

    /**
     * 加密后的密码,盐值为原密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 大陆手机号,唯一性，一个手机只能绑定一个账号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱,唯一性,一个邮箱只能绑定一个账号
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户大头像
     */
    @TableField(value = "big_avatar")
    private String bigAvatar;

    /**
     * 用户头像缩略图
     */
    @TableField(value = "mini_avatar")
    private String miniAvatar;

    /**
     * 用户二维码
     */
    @TableField(value = "qrcode")
    private String qrcode;

    /**
     * 会话唯一标识
     */
    @TableField(value = "cid")
    private String cid;

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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getAccount() == null ? other.getAccount() == null : this.getAccount().equals(other.getAccount()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getBigAvatar() == null ? other.getBigAvatar() == null : this.getBigAvatar().equals(other.getBigAvatar()))
            && (this.getMiniAvatar() == null ? other.getMiniAvatar() == null : this.getMiniAvatar().equals(other.getMiniAvatar()))
            && (this.getQrcode() == null ? other.getQrcode() == null : this.getQrcode().equals(other.getQrcode()))
            && (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getAccount() == null) ? 0 : getAccount().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getBigAvatar() == null) ? 0 : getBigAvatar().hashCode());
        result = prime * result + ((getMiniAvatar() == null) ? 0 : getMiniAvatar().hashCode());
        result = prime * result + ((getQrcode() == null) ? 0 : getQrcode().hashCode());
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", bigAvatar=").append(bigAvatar);
        sb.append(", miniAvatar=").append(miniAvatar);
        sb.append(", qrcode=").append(qrcode);
        sb.append(", cid=").append(cid);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}