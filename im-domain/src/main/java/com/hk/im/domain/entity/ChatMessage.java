package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hk.im.domain.annotation.MeiliSearchIndex;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_chat_message
 */
@TableName(value ="tb_chat_message", autoResultMap = true)
@Data
@Accessors(chain = true)
@MeiliSearchIndex
public class ChatMessage implements Serializable {
    /**
     * 聊天消息表id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息序列号，为发号器分配的id号
     */
    @TableField(value = "sequence")
    private Long sequence;

    /**
     * 消息属性：0.默认，1.离线消息，2.漫游消息，3.同步消息，4.透传消息，5.控制消息
     */
    @TableField(value = "message_feature")
    private Integer messageFeature;

    /**
     * 消息类型:1.文本，2.图片，3.语音，4.图文混合，5.文件，6.语音通话，7.视频通话，
                                8.白板演示，9.远程控制，10.日程安排，11.外部分享,12.@消息，13.红包消息
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 消息内容,最大文本数量1024个字符
     */
    @TableField(value = "content")
    private String content;

    /**
     * 图片，文件，视频，音频等等链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * 扩展字段，一般使用JSON字符串存储,可以用户回复消息，@消息，超文本消息，卡片消息，视频消息等
     */
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private Object extra;

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
    private static final long serialVersionUID = 13571536373L;

}