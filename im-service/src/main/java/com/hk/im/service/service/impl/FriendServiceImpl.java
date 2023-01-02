package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.Friend;
import com.hk.im.infrastructure.mapper.FriendMapper;
import com.hk.im.service.service.FriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : FriendServiceImpl
 * @author : HK意境
 * @date : 2023/1/2 16:48
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService {

    @Resource
    private FriendMapper friendMapper;

    /**
     * 是否好友关系
     * @param fromUserId
     * @param toUserId
     * @return Friend
     */
    @Override
    public Friend isFriendRelationship(Long fromUserId, Long toUserId) {

        Friend friend = this.friendMapper.selectFriendByTowUser(fromUserId, toUserId);
        return friend;
    }


    /**
     * 是否黑名单
     * @param fromUserId
     * @param toUserId
     * @return
     */
    @Override
    public Friend isFriendBlackList(Long fromUserId, Long toUserId) {
        return this.haveFriendRelationship(fromUserId, toUserId, FriendConstants.FriendRelationship.BLACK);
    }

    /**
     * 是否具有某种关系
     * @param fromUserId
     * @param toUserId
     * @param relation
     * @return
     */
    @Override
    public Friend haveFriendRelationship(Long fromUserId, Long toUserId, FriendConstants.FriendRelationship relation) {
        return this.friendMapper.selectTheRelationship(fromUserId, toUserId, relation.ordinal());
    }
}




