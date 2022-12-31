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
 * @TableName tb_group_announcement
 */
@TableName(value ="tb_group_announcement")
@Data
public class GroupAnnouncement implements Serializable {
    /**
     * 公告id
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
     * 群公告类型：1.普通公告，2.置顶公告, 3.新人公告
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 公告撰写者
     */
    @TableField(value = "author")
    private Long author;

    /**
     * 公告标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 公告内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 是否删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

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
        GroupAnnouncement other = (GroupAnnouncement) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getGroupAccount() == null ? other.getGroupAccount() == null : this.getGroupAccount().equals(other.getGroupAccount()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAuthor() == null ? other.getAuthor() == null : this.getAuthor().equals(other.getAuthor()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
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
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAuthor() == null) ? 0 : getAuthor().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", author=").append(author);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}