package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : RemoveGroupMemberRequest
 * @date : 2023/1/20 21:42
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class RemoveGroupMemberRequest {

    // 群id
    private String groupId;

    // 被操作用户id
    private String memberId;

    // 操作执行者：群主， 管理员
    private String operatorId;


}
