package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : FriendApplyRequest
 * @date : 2023/1/2 16:28
 * @description : 加好友加群申请
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class FriendApplyRequest {

    // 1. 加好友，2.加群
    private Integer type;

    // 申请发起人
    private Long fromUserId;
    private String fromUserAccount;

    // 申请接收者
    private Long toUserId;
    private String toUserAccount;

    // 好友问题答案
    private String answer;

    // 申请信息
    private String applyInfo;


}
