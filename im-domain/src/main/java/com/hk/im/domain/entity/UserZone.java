package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户空间表
 * @TableName tb_user_zone
 */
@TableName(value ="tb_user_zone")
@Data
@Accessors(chain = true)
public class UserZone implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 文章id
     */
    @TableField(value = "note_id")
    private Long noteId;

    /**
     * 是否转发别人的动态：false：不是转发, true: 转发
     */
    @TableField(value = "forward")
    private Boolean forward;

    /**
     * 是否删除：表示是否撤回，删除，发布的说说
     */
    @TableField(value = "deleted")
    private Boolean deleted;

    /**
     * 计划发布时间
     */
    @TableField(value = "plan_time")
    private LocalDateTime planTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 跟新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
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
        UserZone other = (UserZone) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNoteId() == null ? other.getNoteId() == null : this.getNoteId().equals(other.getNoteId()))
            && (this.getForward() == null ? other.getForward() == null : this.getForward().equals(other.getForward()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getPlanTime() == null ? other.getPlanTime() == null : this.getPlanTime().equals(other.getPlanTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNoteId() == null) ? 0 : getNoteId().hashCode());
        result = prime * result + ((getForward() == null) ? 0 : getForward().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getPlanTime() == null) ? 0 : getPlanTime().hashCode());
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
        sb.append(", noteId=").append(noteId);
        sb.append(", forward=").append(forward);
        sb.append(", deleted=").append(deleted);
        sb.append(", planTime=").append(planTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}