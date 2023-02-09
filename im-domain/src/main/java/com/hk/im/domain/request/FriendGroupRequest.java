package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : FriendGroupRequest
 * @date : 2023/2/9 20:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class FriendGroupRequest {

    // 操作：1.创建分组,2.删除分组,3.重命名分组,4.移动分组
    private Integer operation;
    private String userId;
    private String groupName;
    private String newName;

    // 移动分组好友
    private List<String> friendList;

}
