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


    public MessageVO computedPrivateMessageVO(UserVO userVO, UserVO friendVO) {

        // 计算消息属主头像：如果消息发送者是当前登录用户，则头像为 当前登录用户头像；否则当前消息属主头像为friendVO头像
        if (this.senderId.equals(userVO.getId())) {
            // 登录用户是消息发送者
            this.setAvatar(userVO.getMiniAvatar());
            this.setSender(userVO);
            this.setSenderAvatar(userVO.getMiniAvatar());
            this.setNickname(userVO.getUsername());
            this.setFriendRemark(userVO.getUsername());
            this.setSenderId(userVO.getId());
        } else {
            // 好友是消息发送者
            this.setAvatar(friendVO.getMiniAvatar());
            this.setSender(friendVO);
            this.setSenderAvatar(friendVO.getMiniAvatar());
            this.setNickname(friendVO.getUsername());
            this.setFriendRemark(friendVO.getUsername());
            this.setSenderId(friendVO.getId());
        }
        return this;
    }



}
