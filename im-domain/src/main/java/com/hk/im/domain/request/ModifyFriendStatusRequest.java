package com.hk.im.domain.request;

import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : ModifyFriendStatusRequest
 * @date : 2023/1/10 20:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class ModifyFriendStatusRequest {

    // 需要被修改为的关系： FriendRelationship 枚举
    private Integer status;

    private String userId;

    private String friendId;


}
