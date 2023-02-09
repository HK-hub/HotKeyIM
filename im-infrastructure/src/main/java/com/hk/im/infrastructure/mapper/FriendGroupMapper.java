package com.hk.im.infrastructure.mapper;

import com.hk.im.domain.entity.FriendGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.hk.im.domain.entity.FriendGroup
 */
public interface FriendGroupMapper extends BaseMapper<FriendGroup> {

    /**
     * 获取默认分组
     * @param userId
     * @return
     */
    @Select("select * from tb_friend_group where user_id = #{userId} and name = '默认分组'")
    public FriendGroup getUserDefaultGroup(@Param("userId") Long userId);

    /**
     * 获取用户指定分组
     * @param userId
     * @param name
     * @return
     */
    @Select("select * from tb_friend_group where user_id = #{userId} and name = #{name}")
    public FriendGroup getTheFriendGroup(@Param("userId") Long userId, @Param("name") String name);

    /**
     * 用户分组增长一个数量
     * @param userId
     * @param name
     * @return
     */
    @Update("update tb_friend_group set count = count + 1 where user_id = #{userId} and name = #{name}")
    public Boolean increaseUserGroupCount(@Param("userId") Long userId, @Param("name")String name);

    /**
     * 用户分组增加指定数量
     * @param userId
     * @param name
     * @param count
     * @return
     */
    @Update("update tb_friend_group set count = count + #{count} where user_id = #{userId} and name = #{name}")
    public Boolean increaseByCountUserGroup(@Param("userId") Long userId, @Param("name")String name, @Param("count") Integer count);

    /**
     * 用户分组好友数量更新为指定数量
     * @param userId
     * @param name
     * @param count
     * @return
     */
    @Update("update tb_friend_group set count = #{count} where user_id = #{userId} and name = #{name}")
    public Boolean updateGroupFriendCount(@Param("userId") Long userId, @Param("name")String name, @Param("count") Integer count);


    /**
     * 删除用户分组
     * @param userId
     * @param name
     * @return
     */
    @Delete("delete from tb_friend_group where user_id = #{userId} and name = #{name}")
    public Boolean deleteUserGroup(@Param("userId") Long userId, @Param("name") String name);






}




