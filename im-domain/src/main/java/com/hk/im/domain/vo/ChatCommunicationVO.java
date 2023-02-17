package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : HK意境
 * @ClassName : ChatCommunicationVO
 * @date : 2023/2/17 14:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ChatCommunicationVO {

    /**
     * 会话表id
     */
    private Long id;

    /**
     * 消息属主
     */
    private Long belongUserId;

    /**
     * 回话id
     */
    private String sessionId;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 接收者id
     */
    private Long receiverId;

    private FriendVO friendVO;

    /**
     * 群聊id,用于扩展群内@消息
     */
    private Long groupId;

    private GroupVO groupVO;

    /**
     * 最后一条消息id
     */
    private Long lastMessageId;

    /**
     * 最后一条消息的内容
     */
    private String lastMessageContent;

    /**
     * 最后的消息发送者名称
     */
    private String lastSenderUsername;

    /**
     * 最后消息发送时间
     */
    private Date lastSendTime;

    /**
     * 回话类型(1.个人聊天，2.群聊消息，3.系统消息,4.控制消息)
     */
    private Integer sessionType;

    /**
     * 会话修改命令（预留）
     */
    private Integer sessionStatus;

    /**
     * 未读消息数量
     */
    private Integer unreadCount;

    /**
     * 草稿信息
     */
    private String draft;

    /**
     * 是否置顶会话：0.否，1.是
     */
    private Boolean top;

    /**
     * 是否聊天机器人：0.否，1.是
     */
    private Boolean robot;


    /**
     * 是否消息免打扰：0.否，1.是
     */
    private Boolean disturb;

    /**
     * 是否在线：0.否，1.是
     */
    private Boolean online;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

}
