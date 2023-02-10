package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : CreateCommunicationRequest
 * @date : 2023/2/10 17:38
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CreateCommunicationRequest {

    // 对话类型[1:私聊;2:群聊;]
    private Integer type;

    // 接受者id: 可能为好友，可能为群聊
    private String receiverId;

    // 当前用户
    private String userId;

}
