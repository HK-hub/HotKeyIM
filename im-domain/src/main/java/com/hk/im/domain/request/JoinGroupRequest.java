package com.hk.im.domain.request;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : JoinGroupRequest
 * @date : 2023/2/13 19:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class JoinGroupRequest {

    private String userId;

    private String groupId;

    // 加群申请
    private String applyId;

    // 申请处理者
    private String handlerId;

    // 处理结果：1.同意，2.拒绝,3.待处理
    private Integer operation;
}
