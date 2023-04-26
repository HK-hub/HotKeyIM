package com.hk.im.domain.request.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : AssignMemberManagePermissionRequest
 * @date : 2023/4/25 16:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class AssignMemberManagePermissionRequest {

    /**
     * 群聊
     */
    private Long groupId;

    /**
     * 1.分配，0.去除 管理员
     */
    private Integer operation;

    /**
     * 操作员
     */
    private Long handlerId;


    /**
     * 被操控者：傀儡
     */
    private Long puppetId;

}
