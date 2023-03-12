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
     * 资源id
     */
    @TableField(value = "resource_id")
    private Long resourceId;

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

}