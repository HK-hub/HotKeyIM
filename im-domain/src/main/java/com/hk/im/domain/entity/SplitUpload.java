package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文件拆分数据表
 * @TableName tb_split_upload
 */
@TableName(value ="tb_split_upload")
@Data
public class SplitUpload implements Serializable {
    /**
     * 临时文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件属性[1:合并文件;2:拆分文件]
     */
    private Integer type;

    /**
     * 驱动类型[1:local;2:cos;]
     */
    private Integer drive;

    /**
     * 临时文件hash名
     */
    private String uploadId;

    /**
     * 上传的用户ID
     */
    private Long userId;

    /**
     * 原文件名
     */
    private String originalName;

    /**
     * 当前索引块
     */
    private Object splitIndex;

    /**
     * 总上传索引块
     */
    private Object splitNum;

    /**
     * 临时保存路径
     */
    private String path;

    /**
     * 文件后缀名
     */
    private String fileExt;

    /**
     * 文件大小
     */
    private Object fileSize;

    /**
     * 文件是否删除[0:否;1:是;] 
     */
    private Integer isDelete;

    /**
     * 额外参数json
     */
    private Object attribute;

    /**
     * 更新时间
     */
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    private LocalDateTime updateTime;

}