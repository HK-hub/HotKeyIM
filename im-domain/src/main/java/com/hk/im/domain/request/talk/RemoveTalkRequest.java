package com.hk.im.domain.request.talk;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : RemoveTalkRequest
 * @date : 2023/5/7 20:55
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RemoveTalkRequest {

    /**
     * 用户id
     */
    public Long userId;

    /**
     * 好友或群聊id
     */
    private Long friendId;


    /**
     * 会话id
     */
    private Long talkId;

}
