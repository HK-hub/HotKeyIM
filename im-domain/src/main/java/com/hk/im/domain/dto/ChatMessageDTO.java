package com.hk.im.domain.dto;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : ChatMessageDTO
 * @date : 2023/2/23 23:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ChatMessageDTO {

    private Long messageId;

    private Long messageFlowId;

    private Long senderId;

    private Long receiverId;

    private Long sequence;

    private Integer talkType;

    private Integer messageType;

    private Integer messageFeature;

    private String content;

    private String url;

    private String extra;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
