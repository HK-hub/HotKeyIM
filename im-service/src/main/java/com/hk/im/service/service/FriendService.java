package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.Friend;
import org.apache.ibatis.annotations.Param;

/**
 *
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
     * 获取用户的黑名单列表
     * @param userId
     * @return
     */
    ResponseResult getUserBlackList(Long userId);

}
