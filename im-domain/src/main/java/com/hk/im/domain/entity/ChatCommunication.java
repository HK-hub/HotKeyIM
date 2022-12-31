package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_chat_communication
 */
@TableName(value ="tb_chat_communication")
@Data
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
    @TableField(value = "acceptor_id")
    private Long acceptorId;

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
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChatCommunication other = (ChatCommunication) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBelongUserId() == null ? other.getBelongUserId() == null : this.getBelongUserId().equals(other.getBelongUserId()))
            && (this.getSessionId() == null ? other.getSessionId() == null : this.getSessionId().equals(other.getSessionId()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getAcceptorId() == null ? other.getAcceptorId() == null : this.getAcceptorId().equals(other.getAcceptorId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getLastMessageId() == null ? other.getLastMessageId() == null : this.getLastMessageId().equals(other.getLastMessageId()))
            && (this.getLastMessageContent() == null ? other.getLastMessageContent() == null : this.getLastMessageContent().equals(other.getLastMessageContent()))
            && (this.getLastSenderUsername() == null ? other.getLastSenderUsername() == null : this.getLastSenderUsername().equals(other.getLastSenderUsername()))
            && (this.getLastSendTime() == null ? other.getLastSendTime() == null : this.getLastSendTime().equals(other.getLastSendTime()))
            && (this.getSessionType() == null ? other.getSessionType() == null : this.getSessionType().equals(other.getSessionType()))
            && (this.getSessionStatus() == null ? other.getSessionStatus() == null : this.getSessionStatus().equals(other.getSessionStatus()))
            && (this.getUnreadCount() == null ? other.getUnreadCount() == null : this.getUnreadCount().equals(other.getUnreadCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBelongUserId() == null) ? 0 : getBelongUserId().hashCode());
        result = prime * result + ((getSessionId() == null) ? 0 : getSessionId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getAcceptorId() == null) ? 0 : getAcceptorId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getLastMessageId() == null) ? 0 : getLastMessageId().hashCode());
        result = prime * result + ((getLastMessageContent() == null) ? 0 : getLastMessageContent().hashCode());
        result = prime * result + ((getLastSenderUsername() == null) ? 0 : getLastSenderUsername().hashCode());
        result = prime * result + ((getLastSendTime() == null) ? 0 : getLastSendTime().hashCode());
        result = prime * result + ((getSessionType() == null) ? 0 : getSessionType().hashCode());
        result = prime * result + ((getSessionStatus() == null) ? 0 : getSessionStatus().hashCode());
        result = prime * result + ((getUnreadCount() == null) ? 0 : getUnreadCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", belongUserId=").append(belongUserId);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", senderId=").append(senderId);
        sb.append(", acceptorId=").append(acceptorId);
        sb.append(", groupId=").append(groupId);
        sb.append(", lastMessageId=").append(lastMessageId);
        sb.append(", lastMessageContent=").append(lastMessageContent);
        sb.append(", lastSenderUsername=").append(lastSenderUsername);
        sb.append(", lastSendTime=").append(lastSendTime);
        sb.append(", sessionType=").append(sessionType);
        sb.append(", sessionStatus=").append(sessionStatus);
        sb.append(", unreadCount=").append(unreadCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}