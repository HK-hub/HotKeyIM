package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

/**
 * @author : HK意境
 * @ClassName : CreateGroupApplyRequest
 * @date : 2023/2/14 11:35
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CreateGroupApplyRequest {

    private String userId;

    private String groupId;

    // 申请信息: 答案或备注信息等
    private String applyInfo;

    /**
     * 申请类型：1.搜索加群，2.邀请入群，3.扫码加群
     */
    private Integer joinType;


}
