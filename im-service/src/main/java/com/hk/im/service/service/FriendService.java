package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.FriendGroup;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.ModifyFriendInfoRequest;
import com.hk.im.domain.request.ModifyFriendStatusRequest;
import org.apache.ibatis.annotations.Param;


/**
 * @ClassName : FriendService
 * @author : HK意境
 * @date : 2023/1/9 19:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface FriendService extends IService<Friend> {

    /**
     * 判断两个用户是否为好友关系
     * @param fromUserId
     * @param toUserId
     * @return
     */
    Friend isFriendRelationship(Long fromUserId, Long toUserId);

    /**
     * 是否黑名单
     * @param fromUserId
     * @param toUserId
     * @return
     */
    Friend isFriendBlackList(Long fromUserId, Long toUserId);


    /**
     * 是否具有某种关系
     * @param fromUserId
     * @param toUserId
     * @return
     */
    Friend haveFriendRelationship(Long fromUserId, Long toUserId, FriendConstants.FriendRelationship relation);

    /**
     * 获取用户好友列表
     * @param userId
     * @return
     */
    ResponseResult getUserFriendList(Long userId);

    /**
     * 获取用户好友列表V2 版本: 只返回好友列表
     * @param userId
     * @return
     */
    ResponseResult getUserFriendListV2(Long userId);


    /**
     * 获取用户的黑名单列表
     * @param userId
     * @return
     */
    ResponseResult getUserBlackList(Long userId);

    /**
     * 修改好友信息
     * @param request
     * @return
     */
    ResponseResult updateFriendInfo(ModifyFriendInfoRequest request);

    /**
     * 删除好友
     * @param friendId
     * @return
     */
    ResponseResult removeFriend(String friendId, User user) throws ApiException;

    /**
     * 修改好友状态：例如，拉黑，置顶，特别关心等
     * @param request
     * @return
     */
    ResponseResult updateFriendStatus(ModifyFriendStatusRequest request);

}
