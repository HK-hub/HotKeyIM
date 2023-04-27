package com.hk.im.domain.request.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : HandoverMasterRequest
 * @date : 2023/4/27 9:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class HandoverMasterRequest {

    private Long groupId;

    private Long memberId;

    private Long userId;
}
