package com.hk.im.domain.response;

import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.vo.FriendGroupVO;
import com.hk.im.domain.vo.FriendVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : FriendListResponse
 * @date : 2023/2/8 20:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class FriendListResponse {

    // 好友列表
    private List<FriendVO> friendList;
    // 好友分组列表
    private List<FriendGroup> groupList;


}
