package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : ClearUnreadRequest
 * @date : 2023/3/2 11:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ClearUnreadRequest {

    // 会话类型
    private Integer talkType;

    // 当前登录用户：将要清空此用户的未读消息
    private String senderId;

    // 消息接收者
    private String receiverId;

}
