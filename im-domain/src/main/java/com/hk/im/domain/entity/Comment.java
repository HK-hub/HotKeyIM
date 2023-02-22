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
 * @TableName tb_comment
 */
@TableName(value ="tb_comment")
@Data
public class Comment implements Serializable {
    /**
     * 说说评论id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 评论者id
     */
    @TableField(value = "commentator_id")
    private Long commentatorId;

    /**
     * 说说作者
     */
    @TableField(value = "post_author_id")
    private Long postAuthorId;

    /**
     * 说说id
     */
    @TableField(value = "post_id")
    private Long postId;

    /**
     * 父id:如果为post_id 标识直接对说说进行评论，为用户id则是评论回复
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 被评论者id:可能为post_id, 可能为 user_id
     */
    @TableField(value = "commentated_id")
    private Long commentatedId;

    /**
     * 评论层级：1.一级评论：对说说评论，>1.回复评论
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 评论内容
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
     * 修改时间
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
        Comment other = (Comment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCommentatorId() == null ? other.getCommentatorId() == null : this.getCommentatorId().equals(other.getCommentatorId()))
            && (this.getPostAuthorId() == null ? other.getPostAuthorId() == null : this.getPostAuthorId().equals(other.getPostAuthorId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getCommentatedId() == null ? other.getCommentatedId() == null : this.getCommentatedId().equals(other.getCommentatedId()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
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
        result = prime * result + ((getCommentatorId() == null) ? 0 : getCommentatorId().hashCode());
        result = prime * result + ((getPostAuthorId() == null) ? 0 : getPostAuthorId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getCommentatedId() == null) ? 0 : getCommentatedId().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
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
        sb.append(", commentatorId=").append(commentatorId);
        sb.append(", postAuthorId=").append(postAuthorId);
        sb.append(", postId=").append(postId);
        sb.append(", parentId=").append(parentId);
        sb.append(", commentatedId=").append(commentatedId);
        sb.append(", level=").append(level);
        sb.append(", content=").append(content);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}