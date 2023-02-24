package com.hk.im.service.service;

import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.entity.Friend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : HK意境
 * @ClassName : FriendApplyServiceImplTest
 * @date : 2023/1/2 22:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class FriendApplyServiceImplTest {

    Long acceptorId = Long.valueOf("1609821255437963266");
    Long senderId = Long.valueOf("1609073304214302721");

    @Test
    void toBeFriend() {
        // 接收者为主体
        Friend acceptor = new Friend();
        acceptor.setUserId(acceptorId);
        acceptor.setFriendId(senderId);
        acceptor.setRelation(FriendConstants.FriendRelationship.FRIEND.ordinal());

        // 发送者为受体
        Friend sender = new Friend();
        acceptor.setUserId(senderId);
        acceptor.setFriendId(acceptorId);
        acceptor.setRelation(FriendConstants.FriendRelationship.FRIEND.ordinal());


    }
}