package com.hk.im.flow.search.document;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : MessageDocument
 * @date : 2023/5/15 22:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MessageDocument {

    // 消息流水 id
    private Long id;

    // 消息id
    private Long messageId;

    // 发送者
    private Long senderId;

    // 接收者
    private Long receiverId;

    // 内容
    private String content;

    private LocalDateTime createTime;

}
