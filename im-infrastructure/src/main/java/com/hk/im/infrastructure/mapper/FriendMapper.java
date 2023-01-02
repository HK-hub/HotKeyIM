package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.hk.im.domain.entity.Friend
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    public Friend selectFriendByTowUser(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    public Friend selectTheRelationship(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId, @Param("relation") int relation);
}




