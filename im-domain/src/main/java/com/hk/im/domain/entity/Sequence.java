package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @ClassName : Sequence
 * @author : HK意境
 * @date : 2023/1/26 18:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@TableName(value ="tb_sequence")
@Data
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class Sequence implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标识业务类型
     */
    @TableField(value = "name")
    private String name;

    /**
     * 业务描述
     */
    private String description;

    /**
     * 会话id
     */
    @TableField(value = "session_id")
    private Long sessionId;

    /**
     * 会话参与者, 消息接收者
     */
    @TableField(value = "receiver_id")
    private Long receiverId;

    /**
     * 会话发起者: 消息发送者
     */
    @TableField(value = "sender_id")
    private Long senderId;

    /**
     * 下一次将要申请的号段起始位置
     */
    @TableField(value = "max")
    private Long max;

    /**
     * 递增步长
     */
    @TableField(value = "step")
    private Integer step;

    /**
     * 号段大小
     */
    @TableField(value = "segment")
    private Integer segment;

    /**
     * 号段大小
     */
    @TableField(value = "version")
    private Long version;


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


    /**
     * 当前ID
     */
    @TableField(exist = false)
    private Long current = 1L;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}