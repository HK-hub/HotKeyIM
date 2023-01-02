package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.PageResult;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.FriendApplyRequest;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.entity.FriendApply;

/**
 * @ClassName : FriendApplyService
 * @author : HK意境
 * @date : 2023/1/2 20:01
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface FriendApplyService extends IService<FriendApply> {

    /**
     * 发现好友
     * @param request
     * @return
     */
    PageResult findFriendAccountOrName(FriendFindRequest request);

    /**
     * 发现群聊
     * @param request
     * @return
     */
    PageResult findGroupAccountOrName(FriendFindRequest request);


    /**
     * 好友申请
     * @param request
     * @return
     */
    ResponseResult applyToBeFriend(FriendApplyRequest request);

    /**
     * 获取一个好友申请记录
     * @param fromUserId
     * @param toUserId
     * @return
     */
    FriendApply getFriendApply(Long fromUserId, Long toUserId);


    /**
     * 获取用户所有发送的有效申请：带验证的
     * @param userId
     * @return
     */
    ResponseResult getUserAllSendFriendApply(Long userId);

    /**
     * 获取用户所有收到的有效好友申请：待验证，同意
     * @param userId
     * @return
     */
    ResponseResult getUserAllReceiveFriendApply(Long userId);

}
