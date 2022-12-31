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
 * @TableName tb_group_member
 */
@TableName(value ="tb_group_member")
@Data
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 群id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 群号
     */
    @TableField(value = "group_account")
    private Long groupAccount;

    /**
     * 群成员id
     */
    @TableField(value = "member_id")
    private Long memberId;

    /**
     * 群成员群外昵称
     */
    @TableField(value = "member_username")
    private String memberUsername;

    /**
     * 群成员的群内昵称
     */
    @TableField(value = "member_remark_name")
    private String memberRemarkName;

    /**
     * 群成员头像(缩略图)
     */
    @TableField(value = "member_avatar")
    private String memberAvatar;

    /**
     * 群员角色:1.普通成员，2.管理员，3.群主
     */
    @TableField(value = "member_role")
    private Integer memberRole;

    /**
     * 群分组，群分类
     */
    @TableField(value = "group_category")
    private String groupCategory;

    /**
     * 禁言时间：表示禁止发言的结束时间
     */
    @TableField(value = "gag_time")
    private LocalDateTime gagTime;

    /**
     * 群状态：1.加群，2.退群，3.群黑名单(前提是已经被踢出群聊)
     */
    @TableField(value = "status")
    private Integer status;

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
        GroupMember other = (GroupMember) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getGroupAccount() == null ? other.getGroupAccount() == null : this.getGroupAccount().equals(other.getGroupAccount()))
            && (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getMemberUsername() == null ? other.getMemberUsername() == null : this.getMemberUsername().equals(other.getMemberUsername()))
            && (this.getMemberRemarkName() == null ? other.getMemberRemarkName() == null : this.getMemberRemarkName().equals(other.getMemberRemarkName()))
            && (this.getMemberAvatar() == null ? other.getMemberAvatar() == null : this.getMemberAvatar().equals(other.getMemberAvatar()))
            && (this.getMemberRole() == null ? other.getMemberRole() == null : this.getMemberRole().equals(other.getMemberRole()))
            && (this.getGroupCategory() == null ? other.getGroupCategory() == null : this.getGroupCategory().equals(other.getGroupCategory()))
            && (this.getGagTime() == null ? other.getGagTime() == null : this.getGagTime().equals(other.getGagTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getGroupAccount() == null) ? 0 : getGroupAccount().hashCode());
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getMemberUsername() == null) ? 0 : getMemberUsername().hashCode());
        result = prime * result + ((getMemberRemarkName() == null) ? 0 : getMemberRemarkName().hashCode());
        result = prime * result + ((getMemberAvatar() == null) ? 0 : getMemberAvatar().hashCode());
        result = prime * result + ((getMemberRole() == null) ? 0 : getMemberRole().hashCode());
        result = prime * result + ((getGroupCategory() == null) ? 0 : getGroupCategory().hashCode());
        result = prime * result + ((getGagTime() == null) ? 0 : getGagTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", groupId=").append(groupId);
        sb.append(", groupAccount=").append(groupAccount);
        sb.append(", memberId=").append(memberId);
        sb.append(", memberUsername=").append(memberUsername);
        sb.append(", memberRemarkName=").append(memberRemarkName);
        sb.append(", memberAvatar=").append(memberAvatar);
        sb.append(", memberRole=").append(memberRole);
        sb.append(", groupCategory=").append(groupCategory);
        sb.append(", gagTime=").append(gagTime);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}