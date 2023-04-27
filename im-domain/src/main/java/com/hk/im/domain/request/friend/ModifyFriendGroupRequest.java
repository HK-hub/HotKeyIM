package com.hk.im.domain.request.friend;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : ModifyFriendGroupRequest
 * @date : 2023/4/27 15:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ModifyFriendGroupRequest {

    private Long friendId;

    private Long groupId;


}
