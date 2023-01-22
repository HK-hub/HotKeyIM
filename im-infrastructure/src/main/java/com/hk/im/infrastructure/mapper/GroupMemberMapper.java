package com.hk.im.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.im.domain.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.hk.im.domain.entity.GroupMember
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    // 根据memberId 和 gourpId 查询群聊成员
    public GroupMember getGroupMemberByGroupIdAndMemberId(@Param("groupId") String groupId, @Param("memberId") String memberId);

    // 修改用户昵称

}




