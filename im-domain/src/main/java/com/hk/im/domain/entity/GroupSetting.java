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
 * @TableName tb_group_setting
 */
@TableName(value ="tb_group_setting")
@Data
public class GroupSetting implements Serializable {
    /**
     * 群id
     */
    @TableId(value = "group_id")
    private Long groupId;

    /**
     * 群号
     */
    @TableField(value = "group_account")
    private Long groupAccount;

    /**
     * 群定位:国家-省份-城市-区-县-镇
     */
    @TableField(value = "position")
    private String position;

    /**
     * 群人数限制:200人，500人，1000人，2000人
     */
    @TableField(value = "member_capacity")
    private Integer memberCapacity;

    /**
     * 发现群方式：1.公开群(支持搜索群名称，群号，群二维码，邀请)，2.不公开群(不支持搜索群名称，支持搜索群号，群二维码，邀请)，3.邀请制(只能通过成员邀请)
     */
    @TableField(value = "find_type")
    private Integer findType;

    /**
     * 加群方式：1.允许任何人everybody,2.需要验证verification, 3.不允许人加群nobody
     */
    @TableField(value = "join_type")
    private Integer joinType;

    /**
     * 全员禁言
     */
    @TableField(value = "forbid_send")
    private Integer forbidSend;

    /**
     * 群最新公告
     */
    @TableField(value = "announcement")
    private String announcement;

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
        GroupSetting other = (GroupSetting) that;
        return (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getGroupAccount() == null ? other.getGroupAccount() == null : this.getGroupAccount().equals(other.getGroupAccount()))
            && (this.getPosition() == null ? other.getPosition() == null : this.getPosition().equals(other.getPosition()))
            && (this.getMemberCapacity() == null ? other.getMemberCapacity() == null : this.getMemberCapacity().equals(other.getMemberCapacity()))
            && (this.getFindType() == null ? other.getFindType() == null : this.getFindType().equals(other.getFindType()))
            && (this.getJoinType() == null ? other.getJoinType() == null : this.getJoinType().equals(other.getJoinType()))
            && (this.getForbidSend() == null ? other.getForbidSend() == null : this.getForbidSend().equals(other.getForbidSend()))
            && (this.getAnnouncement() == null ? other.getAnnouncement() == null : this.getAnnouncement().equals(other.getAnnouncement()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getGroupAccount() == null) ? 0 : getGroupAccount().hashCode());
        result = prime * result + ((getPosition() == null) ? 0 : getPosition().hashCode());
        result = prime * result + ((getMemberCapacity() == null) ? 0 : getMemberCapacity().hashCode());
        result = prime * result + ((getFindType() == null) ? 0 : getFindType().hashCode());
        result = prime * result + ((getJoinType() == null) ? 0 : getJoinType().hashCode());
        result = prime * result + ((getForbidSend() == null) ? 0 : getForbidSend().hashCode());
        result = prime * result + ((getAnnouncement() == null) ? 0 : getAnnouncement().hashCode());
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
        sb.append(", groupId=").append(groupId);
        sb.append(", groupAccount=").append(groupAccount);
        sb.append(", position=").append(position);
        sb.append(", memberCapacity=").append(memberCapacity);
        sb.append(", findType=").append(findType);
        sb.append(", joinType=").append(joinType);
        sb.append(", forbidSend=").append(forbidSend);
        sb.append(", announcement=").append(announcement);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}