package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : ClearUnreadRequest
 * @date : 2023/3/2 11:12
 * @description : 清空我的会话未读，说明需要清除我接收到到的消息未读数，那么接收者就是我自己(当前登录用户)，发送者就是好友
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

    // 发送者
    private String senderId;

    // 消息接收者:将要清空此用户的未读消息
    private String receiverId;

}
