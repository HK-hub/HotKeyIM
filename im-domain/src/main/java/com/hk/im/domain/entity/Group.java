package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName tb_group
 */
@TableName(value ="tb_group")
@Data
public class Group implements Serializable {
    /**
     * 群id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 群账号
     */
    @TableField(value = "group_account")
    private Long groupAccount;

    /**
     * 群聊名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 群聊头像
     */
    @TableField(value = "group_avatar")
    private String groupAvatar;

    /**
     * 群描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生
     */
    @TableField(value = "group_type")
    private Integer groupType;

    /**
     * 群二维码
     */
    @TableField(value = "qrcode")
    private String qrcode;

    /**
     * 群人数
     */
    @TableField(value = "member_count")
    private Integer memberCount;

    /**
     * 群主
     */
    @TableField(value = "group_master")
    private Long groupMaster;

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
        Group other = (Group) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupAccount() == null ? other.getGroupAccount() == null : this.getGroupAccount().equals(other.getGroupAccount()))
            && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getGroupAvatar() == null ? other.getGroupAvatar() == null : this.getGroupAvatar().equals(other.getGroupAvatar()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getGroupType() == null ? other.getGroupType() == null : this.getGroupType().equals(other.getGroupType()))
            && (this.getQrcode() == null ? other.getQrcode() == null : this.getQrcode().equals(other.getQrcode()))
            && (this.getMemberCount() == null ? other.getMemberCount() == null : this.getMemberCount().equals(other.getMemberCount()))
            && (this.getGroupMaster() == null ? other.getGroupMaster() == null : this.getGroupMaster().equals(other.getGroupMaster()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupAccount() == null) ? 0 : getGroupAccount().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getGroupAvatar() == null) ? 0 : getGroupAvatar().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getGroupType() == null) ? 0 : getGroupType().hashCode());
        result = prime * result + ((getQrcode() == null) ? 0 : getQrcode().hashCode());
        result = prime * result + ((getMemberCount() == null) ? 0 : getMemberCount().hashCode());
        result = prime * result + ((getGroupMaster() == null) ? 0 : getGroupMaster().hashCode());
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
        sb.append(", groupAccount=").append(groupAccount);
        sb.append(", groupName=").append(groupName);
        sb.append(", groupAvatar=").append(groupAvatar);
        sb.append(", description=").append(description);
        sb.append(", groupType=").append(groupType);
        sb.append(", qrcode=").append(qrcode);
        sb.append(", memberCount=").append(memberCount);
        sb.append(", groupMaster=").append(groupMaster);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}