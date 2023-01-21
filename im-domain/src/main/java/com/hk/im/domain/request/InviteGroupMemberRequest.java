package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : InviteGroupMemberRequest
 * @date : 2023/1/21 22:00
 * @description : 邀请用户加群
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class InviteGroupMemberRequest {

    // 被邀请者：invitee
    private String inviteeId;

    // 邀请者：inviter
    private String inviterId;

    // 群
    private String groupId;



}
