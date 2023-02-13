package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

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
@Accessors
public class JoinGroupRequest {

    @NonNull
    private String userId;

    @NonNull
    private String groupId;

    // 加群申请
    private String applyId;

    // 申请处理者
    private String handlerId;

    // 处理结果：1.同意，2.拒绝
    private Integer operation;
}
