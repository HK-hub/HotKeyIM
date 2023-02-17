package com.hk.im.service.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.request.UserFindPolicyRequest;

/**
 * @author : HK意境
 * @ClassName : FindServiceImpl
 * @date : 2023/2/16 22:32
 * @description : 发现服务
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface FindService {

    /**
     * 获取公开群聊
     * @param request
     * @return
     */
    ResponseResult getPublicGroups(UserFindPolicyRequest request);

    /**
     * 查询群聊
     * @param request
     * @return
     */
    ResponseResult searchForGroup(FriendFindRequest request);
}
