package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.CreateRoomRequest;

/**
 * @author : HK意境
 * @ClassName : ConversationService
 * @date : 2023/3/23 10:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface ConversationService {

    // 创建一个房间
    public ResponseResult createRoom(CreateRoomRequest request);


}
