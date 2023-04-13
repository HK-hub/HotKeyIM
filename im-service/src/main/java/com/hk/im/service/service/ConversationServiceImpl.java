package com.hk.im.service.service;

import com.hk.im.client.service.ConversationService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.CreateRoomRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : HK意境
 * @ClassName : ConversationServiceImpl
 * @date : 2023/3/23 10:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class ConversationServiceImpl implements ConversationService {


    /**
     * 创建一个视频通话房间
     * @param request
     * @return
     */
    @Override
    public ResponseResult createRoom(CreateRoomRequest request) {



        return null;
    }
}
