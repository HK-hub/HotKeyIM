package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户聊天记录（文件消息）
 * @TableName tb_message_file
 */
@TableName(value ="tb_message_file")
@Data
public class MessageFile implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息记录ID
     */
    @TableField(value = "message_id")
    private Long messageId;

    /**
     * 上传文件的用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 文件来源[1:用户上传;2:表情包;]
     */
    @TableField(value = "source")
    private Integer source;

    /**
     * 文件类型[1:图片;2:音频文件;3:视频文件;4:其它文件;]
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 驱动类型[1:local;2:cos;]
     */
    @TableField(value = "drive")
    private Integer drive;

    /**
     * 原文件名
     */
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 文件后缀
     */
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 文件大小
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 文件地址(相对地址)
     */
    @TableField(value = "path")
    private String path;

    /**
     * 网络地址(公开文件地址)
     */
    @TableField(value = "url")
    private String url;

    /**
     * 创建时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

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
        MessageFile other = (MessageFile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMessageId() == null ? other.getMessageId() == null : this.getMessageId().equals(other.getMessageId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDrive() == null ? other.getDrive() == null : this.getDrive().equals(other.getDrive()))
            && (this.getOriginalName() == null ? other.getOriginalName() == null : this.getOriginalName().equals(other.getOriginalName()))
            && (this.getSuffix() == null ? other.getSuffix() == null : this.getSuffix().equals(other.getSuffix()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMessageId() == null) ? 0 : getMessageId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDrive() == null) ? 0 : getDrive().hashCode());
        result = prime * result + ((getOriginalName() == null) ? 0 : getOriginalName().hashCode());
        result = prime * result + ((getSuffix() == null) ? 0 : getSuffix().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", messageId=").append(messageId);
        sb.append(", userId=").append(userId);
        sb.append(", source=").append(source);
        sb.append(", type=").append(type);
        sb.append(", drive=").append(drive);
        sb.append(", originalName=").append(originalName);
        sb.append(", suffix=").append(suffix);
        sb.append(", size=").append(size);
        sb.append(", path=").append(path);
        sb.append(", url=").append(url);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}