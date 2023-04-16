package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.hk.im.domain.annotation.MeiliSearchIndex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_message_flow
 */
@TableName(value ="tb_message_flow")
@Data
@Accessors(chain = true)
@EqualsAndHashCode
@MeiliSearchIndex
public class MessageFlow implements Serializable {
    /**
     * 聊天消息表id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 群id,如果是群聊的话
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 消息发送者id
     */
    @TableField(value = "sender_id")
    private Long senderId;

    /**
     * 消息接收者id(用户id或群id)
     */
    @TableField(value = "receiver_id")
    private Long receiverId;

    /**
     * 会话类型:1.个人聊天,2.群聊，3.系统消息,
     */
    @TableField(value = "chat_type")
    private Integer chatType;

    /**
     * 消息类型:1.文本，2.图片，3.语音，4.图文混合，5.文件，6.语音通话，7.视频通话，
                                8.白板演示，9.远程控制，10.日程安排，11.外部分享,12.@消息
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 聊天消息id
     */
    @TableField(value = "message_id")
    private Long messageId;

    /**
     * 消息序列号
     */
    @TableField(value = "`sequence`")
    private Long sequence;

    /**
     * 消息发送状态：1.发送中，2.已发送，3.发送失败,4.草稿，
     */
    @TableField(value = "send_status")
    private Integer sendStatus;

    /**
     * 签收状态：1.未读，2.已读，3.忽略，4.撤回，5.删除
     */
    @TableField(value = "sign_flag")
    private Integer signFlag;

    /**
     * 是否撤回
     */
    @TableField(value = "revoke")
    private Boolean revoke;

    /**
     * 是否删除该条聊天记录,0.false, 1.ture
     */
    @TableField(value = "deleted")
    private Boolean deleted;

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