package com.hk.im.domain.entity;

import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.hk.im.common.util.FileUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName tb_cloud_disk_resource
 */
@TableName(value = "tb_cloud_resource")
@Data
@Accessors(chain = true)
public class CloudResource implements Serializable {

    /**
     * 云盘id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件资源属主：userId 或者 groupId
     */
    @TableField(value = "belong_id")
    private Long belongId;

    /**
     * 是否是文件夹路径
     */
    @TableField(value = "directory")
    private Boolean directory;


    /**
     * 资源类型：1.图片，2.视频，3.音频，4.压缩包，5.文件
     */
    @TableField(value = "resource_type")
    private Integer resourceType;

    /**
     * 资源名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 资源描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 资源扩展名(文件扩展名)
     */
    @TableField(value = "extend_type")
    private String extendType;

    /**
     * 文件 hash 值
     */
    @TableField(value = "hash")
    private String hash;


    /**
     * 资源文件md5
     */
    @TableField(value = "md5")
    private String md5;

    /**
     * 文件url链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * 文件大小单位字节 byte
     */
    @TableField(value = "`size`")
    private Long size;


    /**
     * 实现秒传功能：count 用于计数，当count == 0 的时候，发送事件/消息/任务进行删除
     */
    @TableField(value = "`count`")
    private Integer count;

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
        CloudResource other = (CloudResource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getResourceType() == null ? other.getResourceType() == null : this.getResourceType().equals(other.getResourceType()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getExtendType() == null ? other.getExtendType() == null : this.getExtendType().equals(other.getExtendType()))
                && (this.getMd5() == null ? other.getMd5() == null : this.getMd5().equals(other.getMd5()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getResourceType() == null) ? 0 : getResourceType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getExtendType() == null) ? 0 : getExtendType().hashCode());
        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
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
        sb.append(", resourceType=").append(resourceType);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", extendType=").append(extendType);
        sb.append(", md5=").append(md5);
        sb.append(", url=").append(url);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 资源类型：1.图片，2.视频，3.音频，4.压缩包，5.文件
     */
    public static enum ResourceType {

        UNKNOWN,
        IMAGE,
        VOICE,
        VIDEO,
        COMPRESS,
        TEXT,
        CODE,
        FILE,

    }


    /**
     * 获取文件的资源分类
     *
     * @param fileName
     *
     * @return
     */
    public static int getResourceType(String fileName) {
        String suffix = FileNameUtil.getSuffix(fileName);
        if (FileUtil.isImage(suffix)) {
            return ResourceType.IMAGE.ordinal();
        } else if (FileUtil.isVideo(suffix)) {
            return ResourceType.VIDEO.ordinal();
        } else if (FileUtil.isVoice(suffix)) {
            return ResourceType.VOICE.ordinal();
        } else if (FileUtil.isCompress(suffix)) {
            return ResourceType.COMPRESS.ordinal();
        } else if (FileUtil.isText(suffix)){
            return ResourceType.TEXT.ordinal();
        } else if (FileUtil.isCode(suffix)){
            return ResourceType.CODE.ordinal();
        } else {
            return ResourceType.FILE.ordinal();
        }
    }


}