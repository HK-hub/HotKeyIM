package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.Friend
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    public Friend selectFriendByTowUser(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    public Friend selectTheRelationship(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId, @Param("relation") int relation);

    // 删除好友:删除n条记录，返回影响行数n
    public Integer deleteByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    // 检索所有可以邀请好友
    @Select("select * from tb_friend where user_id = #{userId} and `group` != '黑名单'")
    List<Friend> selectAllFriends(@Param("userId") String userId);

    // 根据关键字查询好友
    List<Friend> selectFriendByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}




