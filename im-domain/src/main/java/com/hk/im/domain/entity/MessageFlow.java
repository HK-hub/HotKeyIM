package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_message_flow
 */
@TableName(value ="tb_message_flow")
@Data
@Accessors(chain = true)
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
    @TableField(value = "acceptor_id")
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
     * 是否删除该条聊天记录,0.false, 1.ture
     */
    @TableField(value = "deleted")
    private Integer deleted;

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
        MessageFlow other = (MessageFlow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
            && (this.getReceiverId() == null ? other.getReceiverId() == null : this.getReceiverId().equals(other.getReceiverId()))
            && (this.getChatType() == null ? other.getChatType() == null : this.getChatType().equals(other.getChatType()))
            && (this.getMessageType() == null ? other.getMessageType() == null : this.getMessageType().equals(other.getMessageType()))
            && (this.getMessageId() == null ? other.getMessageId() == null : this.getMessageId().equals(other.getMessageId()))
            && (this.getSendStatus() == null ? other.getSendStatus() == null : this.getSendStatus().equals(other.getSendStatus()))
            && (this.getSignFlag() == null ? other.getSignFlag() == null : this.getSignFlag().equals(other.getSignFlag()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getReceiverId() == null) ? 0 : getReceiverId().hashCode());
        result = prime * result + ((getChatType() == null) ? 0 : getChatType().hashCode());
        result = prime * result + ((getMessageType() == null) ? 0 : getMessageType().hashCode());
        result = prime * result + ((getMessageId() == null) ? 0 : getMessageId().hashCode());
        result = prime * result + ((getSendStatus() == null) ? 0 : getSendStatus().hashCode());
        result = prime * result + ((getSignFlag() == null) ? 0 : getSignFlag().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
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
        sb.append(", groupId=").append(groupId);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", chatType=").append(chatType);
        sb.append(", messageType=").append(messageType);
        sb.append(", messageId=").append(messageId);
        sb.append(", sendStatus=").append(sendStatus);
        sb.append(", signFlag=").append(signFlag);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}