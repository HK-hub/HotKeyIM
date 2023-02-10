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
 * 用户个性化设置
 * @TableName tb_user_setting
 */
@TableName(value ="tb_user_setting")
@Data
@Accessors(chain = true)
public class UserSetting implements Serializable {
    /**
     * 主键
     */
    @TableField(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 个人名片背景
     */
    @TableField(value = "card_background")
    private String cardBackground;

    /**
     * 聊天背景
     */
    @TableField(value = "talk_background")
    private String talkBackground;

    /**
     * 主题模式：0.居中，1.全屏
     */
    @TableField(value = "theme")
    private Integer theme;

    /**
     * 新消息提示音：1.开去，0.关闭
     */
    @TableField(value = "new_message_remind")
    private Integer newMessageRemind;

    /**
     * 系统消息通知：0.关闭，1.开启
     */
    @TableField(value = "message_notify")
    private Integer messageNotify;

    /**
     * 
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 
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
        UserSetting other = (UserSetting) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCardBackground() == null ? other.getCardBackground() == null : this.getCardBackground().equals(other.getCardBackground()))
            && (this.getTalkBackground() == null ? other.getTalkBackground() == null : this.getTalkBackground().equals(other.getTalkBackground()))
            && (this.getTheme() == null ? other.getTheme() == null : this.getTheme().equals(other.getTheme()))
            && (this.getNewMessageRemind() == null ? other.getNewMessageRemind() == null : this.getNewMessageRemind().equals(other.getNewMessageRemind()))
            && (this.getMessageNotify() == null ? other.getMessageNotify() == null : this.getMessageNotify().equals(other.getMessageNotify()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCardBackground() == null) ? 0 : getCardBackground().hashCode());
        result = prime * result + ((getTalkBackground() == null) ? 0 : getTalkBackground().hashCode());
        result = prime * result + ((getTheme() == null) ? 0 : getTheme().hashCode());
        result = prime * result + ((getNewMessageRemind() == null) ? 0 : getNewMessageRemind().hashCode());
        result = prime * result + ((getMessageNotify() == null) ? 0 : getMessageNotify().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", cardBackground=").append(cardBackground);
        sb.append(", talkBackground=").append(talkBackground);
        sb.append(", theme=").append(theme);
        sb.append(", newMessageRemind=").append(newMessageRemind);
        sb.append(", messageNotify=").append(messageNotify);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}