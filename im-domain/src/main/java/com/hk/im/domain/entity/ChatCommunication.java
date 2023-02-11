package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_chat_communication
 */
@Data
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@TableName(value ="tb_chat_communication")
public class ChatCommunication implements Serializable {
    /**
     * 会话表id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息属主
     */
    @TableField(value = "belong_user_id")
    private Long belongUserId;

    /**
     * 回话id
     */
    @TableField(value = "session_id")
    private String sessionId;

    /**
     * 发送者id
     */
    @TableField(value = "sender_id")
    private Long senderId;

    /**
     * 接收者id
     */
    @TableField(value = "receiver_id")
    private Long receiverId;

    /**
     * 群聊id,用于扩展群内@消息
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 最后一条消息id
     */
    @TableField(value = "last_message_id")
    private Long lastMessageId;

    /**
     * 最后一条消息的内容
     */
    @TableField(value = "last_message_content")
    private String lastMessageContent;

    /**
     * 最后的消息发送者名称
     */
    @TableField(value = "last_sender_username")
    private String lastSenderUsername;

    /**
     * 最后消息发送时间
     */
    @TableField(value = "last_send_time")
    private Date lastSendTime;

    /**
     * 回话类型(1.个人聊天，2.群聊消息，3.系统消息,4.控制消息)
     */
    @TableField(value = "session_type")
    private Integer sessionType;

    /**
     * 会话修改命令（预留）
     */
    @TableField(value = "session_status")
    private Integer sessionStatus;

    /**
     * 未读消息数量
     */
    @TableField(value = "unread_count")
    private Integer unreadCount;

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


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}