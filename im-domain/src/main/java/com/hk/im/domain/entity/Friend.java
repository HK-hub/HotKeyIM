package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName tb_friend
 */
@TableName(value ="tb_friend")
@Data
public class Friend implements Serializable {
    /**
     * 好友关系id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long user_id;

    /**
     * 好友id
     */
    @TableField(value = "friend_id")
    private Long friend_id;

    /**
     * 状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心，4.删除
     */
    @TableField(value = "relation")
    private Integer relation;

    /**
     * 分组:如果不是好友，默认临时会话
     */
    @TableField(value = "group")
    private String group;

    /**
     * 备注姓名
     */
    @TableField(value = "remark_name")
    private String remark_name;

    /**
     * 备注信息
     */
    @TableField(value = "remark_info")
    private String remark_info;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime create_time;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime update_time;

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
        Friend other = (Friend) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getFriend_id() == null ? other.getFriend_id() == null : this.getFriend_id().equals(other.getFriend_id()))
            && (this.getRelation() == null ? other.getRelation() == null : this.getRelation().equals(other.getRelation()))
            && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()))
            && (this.getRemark_name() == null ? other.getRemark_name() == null : this.getRemark_name().equals(other.getRemark_name()))
            && (this.getRemark_info() == null ? other.getRemark_info() == null : this.getRemark_info().equals(other.getRemark_info()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
            && (this.getUpdate_time() == null ? other.getUpdate_time() == null : this.getUpdate_time().equals(other.getUpdate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getFriend_id() == null) ? 0 : getFriend_id().hashCode());
        result = prime * result + ((getRelation() == null) ? 0 : getRelation().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getRemark_name() == null) ? 0 : getRemark_name().hashCode());
        result = prime * result + ((getRemark_info() == null) ? 0 : getRemark_info().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        result = prime * result + ((getUpdate_time() == null) ? 0 : getUpdate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", friend_id=").append(friend_id);
        sb.append(", relation=").append(relation);
        sb.append(", group=").append(group);
        sb.append(", remark_name=").append(remark_name);
        sb.append(", remark_info=").append(remark_info);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}