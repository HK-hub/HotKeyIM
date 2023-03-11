package com.hk.im.domain.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : SplitUploadRequest
 * @date : 2023/3/10 17:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SplitUploadRequest {

    /**
     * 临时文件ID
     */
    private Object id;

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
    private Object splitIndex;

    /**
     * 总上传索引块
     */
    @TableField(value = "split_num")
    private Object splitNum;

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
    private Object fileSize;

    /**
     * 文件是否删除[0:否;1:是;]
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 额外参数json
     */
    @TableField(value = "attribute")
    private Object attribute;

    /**
     * 更新时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;


}
