package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : QuitGroupRequest
 * @date : 2023/1/20 21:42
 * @description : 退出群聊
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class QuitGroupRequest {

    // 群id
    private Long groupId;

    // 被操作用户id
    private Long memberId;

}
