package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.hk.im.domain.entity.GroupMember
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    // 根据memberId 和 gourpId 查询群聊成员
    public GroupMember getGroupMemberByGroupIdAndMemberId(@Param("groupId") String groupId, @Param("memberId") String memberId);

    // 获取群聊成员列表
    List<GroupMember> selectGroupMemberList(@Param("groupId") Long groupId);

    // 获取用户管理群聊
    public List<GroupMember> selectGroupManageGroupList(@Param("memberId") Long memberId);

    // 查询用户加入的群聊列表
    List<GroupMember> selectGroupJoinedGroupList(@Param("memberId") Long memberId);

    // 修改用户昵称

}




