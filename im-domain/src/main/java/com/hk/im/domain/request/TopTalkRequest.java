package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : TopTalkRequest
 * @date : 2023/2/28 12:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class TopTalkRequest {

    // 会话id
    private String talkId;

    // 当前登录用户id
    private String userId;

    // 需要置顶会话好友id
    private String friendId;

    // 操作：1.置顶，2.取消置顶
    private Integer operation;


    public static enum Operation {
        DEFAULT,
        TOP,
        CANCEL,
    }


}
