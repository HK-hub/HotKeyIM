package com.hk.im.domain.request.friend;

import com.hk.im.domain.entity.FriendGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : EditFriendGroupListRequest
 * @date : 2023/4/27 20:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditFriendGroupListRequest {

    private Long userId;

    private List<FriendGroup> friendGroupList;

}
