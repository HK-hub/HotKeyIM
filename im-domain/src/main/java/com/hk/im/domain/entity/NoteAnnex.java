package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 笔记附件
 * @TableName tb_note_annex
 */
@TableName(value ="tb_note_annex")
@Data
@ToString
@Accessors(chain = true)
public class NoteAnnex implements Serializable {
    /**
     * 主键：附件id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 笔记id
     */
    @TableField(value = "note_id")
    private Long noteId;

    /**
     * 文件后缀名
     */
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 附件大小：字节
     */
    @TableField(value = "`size`")
    private Integer size;

    /**
     * 附件名称
     */
    @TableField(value = "original_name")
    private String originalName;

    /**
     * 附件地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 是否删除：用于回收站处理
     */
    @TableField(value = "deleted")
    private Boolean deleted;

    /**
     *
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}