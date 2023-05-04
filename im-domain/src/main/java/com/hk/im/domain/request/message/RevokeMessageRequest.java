package com.hk.im.domain.request.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : RevokeMessageRequest
 * @date : 2023/4/28 17:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RevokeMessageRequest {

    /**
     * 撤回者
     */
    private Long handlerId;

    /**
     * 消息id
     */
    private Long messageId;

}
