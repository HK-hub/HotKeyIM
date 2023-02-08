package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @TableName tb_friend
 */
@TableName(value = "tb_friend")
@Data
@ToString
@EqualsAndHashCode
public class Friend implements Serializable {
    /**
     * 好友关系id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 好友id
     */
    @TableField(value = "friend_id")
    private Long friendId;

    /**
     * 状态：0.陌生人(临时会话)，1.好友，2.黑名单，3.特别关心，4.删除
     */
    @TableField(value = "relation")
    private Integer relation;

    /**
     * 分组:如果不是好友，默认临时会话
     */
    @TableField(value = "`group`")
    private String group;

    /**
     * 分组id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 备注姓名
     */
    @TableField(value = "remark_name")
    private String remarkName;

    /**
     * 备注信息
     */
    @TableField(value = "remark_info")
    private String remarkInfo;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}