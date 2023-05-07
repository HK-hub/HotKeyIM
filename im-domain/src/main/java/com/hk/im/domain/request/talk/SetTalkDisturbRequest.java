package com.hk.im.domain.request.talk;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SetTalkDisturbRequest
 * @date : 2023/5/7 22:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SetTalkDisturbRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接收者id:好友或群聊
     */
    private Long receiverId;


    /**
     * 会话类型
     */
    private Integer talkType;


    /**
     * 会话id
     */
    private Long talkId;


    /**
     * 是否运行打扰: 1.允许打扰，0.不允许打扰
     */
    private Boolean disturb;

}
