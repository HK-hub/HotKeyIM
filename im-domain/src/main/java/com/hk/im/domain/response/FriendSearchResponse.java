package com.hk.im.domain.response;

import com.hk.im.domain.vo.UserVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : FriendSearchResponse
 * @date : 2023/1/30 22:00
 * @description : 搜索好友响应结果
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class FriendSearchResponse {

    // 搜索结果
    private UserVO user;

    // 和搜索人是否好友关系: 1.非好友，2.好友
    private Integer status;


}
