package com.hk.im.domain.vo;

import com.hk.im.domain.bo.MessageBO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : MessageVO
 * @date : 2023/2/26 11:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MessageVO extends MessageBO {

    // 消息发送者
    protected UserVO sender;

    protected FriendVO receiver;

    // 消息发送者头像
    protected String avatar;

    // 发送者头像
    protected String senderAvatar;

    // 接收者头像
    protected String receiverAvatar;

    // 好友(接收者)备注名称
    protected String friendRemark;

    // 好友昵称
    protected String nickname;

}
