package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @ClassName : CloudFile
 * @author : HK意境
 * @date : 2023/4/10 22:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_cloud_file")
public class CloudFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /**
     * 地址
     */
    private String url;

    /**
     * 客户id
     */
    private Long userId;

    /**
     * 文件名字
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 是否收藏
     */
    private Boolean collection;

    /**
     * 文件路径
     */
    private  String fDir;

    /**
     * 类型
     */
    private String filetype;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 文件大小
     */
    private Long size;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
