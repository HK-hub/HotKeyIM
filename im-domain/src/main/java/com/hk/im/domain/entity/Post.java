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
 * @TableName tb_post
 */
@TableName(value ="tb_post")
@Data
public class Post implements Serializable {
    /**
     * 说说id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 说说标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 文本内容
     */
    @TableField(value = "text_content")
    private String textContent;

    /**
     * 图片内容，以;分号隔开，最多九张图片
     */
    @TableField(value = "image_content")
    private String imageContent;

    /**
     * 浏览量
     */
    @TableField(value = "view_num")
    private Integer viewNum;

    /**
     * 点赞量
     */
    @TableField(value = "like_num")
    private Integer likeNum;

    /**
     * 转发量
     */
    @TableField(value = "share_num")
    private Integer shareNum;

    /**
     * 收藏量
     */
    @TableField(value = "collect_num")
    private Integer collectNum;

    /**
     * 批量量
     */
    @TableField(value = "comment_num")
    private Integer commentNum;

    /**
     * 1.草稿，2.发布，3.撤回
     */
    @TableField(value = "status")
    private Integer status;

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
        Post other = (Post) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getTextContent() == null ? other.getTextContent() == null : this.getTextContent().equals(other.getTextContent()))
            && (this.getImageContent() == null ? other.getImageContent() == null : this.getImageContent().equals(other.getImageContent()))
            && (this.getViewNum() == null ? other.getViewNum() == null : this.getViewNum().equals(other.getViewNum()))
            && (this.getLikeNum() == null ? other.getLikeNum() == null : this.getLikeNum().equals(other.getLikeNum()))
            && (this.getShareNum() == null ? other.getShareNum() == null : this.getShareNum().equals(other.getShareNum()))
            && (this.getCollectNum() == null ? other.getCollectNum() == null : this.getCollectNum().equals(other.getCollectNum()))
            && (this.getCommentNum() == null ? other.getCommentNum() == null : this.getCommentNum().equals(other.getCommentNum()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getTextContent() == null) ? 0 : getTextContent().hashCode());
        result = prime * result + ((getImageContent() == null) ? 0 : getImageContent().hashCode());
        result = prime * result + ((getViewNum() == null) ? 0 : getViewNum().hashCode());
        result = prime * result + ((getLikeNum() == null) ? 0 : getLikeNum().hashCode());
        result = prime * result + ((getShareNum() == null) ? 0 : getShareNum().hashCode());
        result = prime * result + ((getCollectNum() == null) ? 0 : getCollectNum().hashCode());
        result = prime * result + ((getCommentNum() == null) ? 0 : getCommentNum().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", textContent=").append(textContent);
        sb.append(", imageContent=").append(imageContent);
        sb.append(", viewNum=").append(viewNum);
        sb.append(", likeNum=").append(likeNum);
        sb.append(", shareNum=").append(shareNum);
        sb.append(", collectNum=").append(collectNum);
        sb.append(", commentNum=").append(commentNum);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}