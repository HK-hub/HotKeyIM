package com.hk.im.domain.request.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : DismissGroupRequest
 * @date : 2023/4/27 10:57
 * @description : 解散群聊
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class DismissGroupRequest {

    private Long operatorId;

    private Long groupId;

}
