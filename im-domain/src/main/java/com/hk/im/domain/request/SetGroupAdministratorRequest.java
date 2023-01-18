package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SetGroupAdministratorRequest
 * @date : 2023/1/18 22:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SetGroupAdministratorRequest {

    // 群id
    private String groupId;

    // 被操作用户id
    private String memberId;

    // 操作执行者：群主
    private String masterId;

    // 设置 or 取消: true=设为管理员，false=取消管理员
    private Boolean set;

}
