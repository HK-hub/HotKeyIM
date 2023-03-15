package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件拆分数据表
 * @TableName tb_split_upload
 */
@TableName(value ="tb_split_upload")
@Data
@Accessors(chain = true)
public class SplitUpload implements Serializable {
    /**
     * 临时文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件属性[1:合并文件;2:拆分文件]
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 驱动类型[1:local;2:cos;]
     */
    @TableField(value = "drive")
    private Integer drive;

    /**
     * 临时文件hash名
     */
    @TableField(value = "upload_id")
    private String uploadId;

    /**
     * 上传的用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 原文件名
     */
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 当前索引块
     */
    @TableField(value = "split_index")
    private Integer splitIndex;

    /**
     * 总上传索引块
     */
    @TableField(value = "split_num")
    private Integer splitNum;

    /**
     * 临时保存路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 文件后缀名
     */
    @TableField(value = "file_ext")
    private String fileExt;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Integer fileSize;

    /**
     * 文件是否删除[0:否;1:是;] 
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;


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


    public static enum FileType {

        UNKNOWN,
        MERGE,
        SPLIT,

    }

}