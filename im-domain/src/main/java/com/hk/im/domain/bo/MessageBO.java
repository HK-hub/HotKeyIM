package com.hk.im.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : MessageBO
 * @date : 2023/2/22 10:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MessageBO {


    /**
     * 聊天消息表id
     */
    private Long id;

    /**
     * 聊天消息id
     */
    private Long messageId;


    /**
     * 聊天消息流水id
     */
    private Long messageFlowId;

    /**
     * 群id,如果是群聊的话
     */
    private Long groupId;

    /**
     * 消息发送者id
     */
    private Long senderId;

    /**
     * 消息接收者id(用户id或群id)
     */
    private Long receiverId;

    /**
     * 会话类型:1.个人聊天,2.群聊，3.系统消息,
     */
    private Integer chatType;

    /**
     * 消息类型:1.文本，2.图片，3.语音，4.图文混合，5.文件，6.语音通话，7.视频通话，
     8.白板演示，9.远程控制，10.日程安排，11.外部分享,12.@消息
     */
    private Integer messageType;

    /**
     * 消息序列号
     */
    private Long sequence;

    /**
     * 消息发送状态：1.发送中，2.已发送，3.发送失败,4.草稿，
     */
    private Integer sendStatus;

    /**
     * 签收状态：1.未读，2.已读，3.忽略，4.撤回，5.删除
     */
    private Integer signFlag;

    /**
     * 是否删除该条聊天记录,0.false, 1.ture
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 消息属性：0.默认，1.离线消息，2.漫游消息，3.同步消息，4.透传消息，5.控制消息
     */
    private Integer messageFeature;

    /**
     * 消息内容,最大文本数量1024个字符
     */
    private String content;

    /**
     * 扩展字段，一般使用JSON字符串存储,可以用户回复消息，@消息，超文本消息，卡片消息，视频消息等
     */
    private Map<String, Object> extra;


}
