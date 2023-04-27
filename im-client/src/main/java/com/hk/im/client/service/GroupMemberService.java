package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.group.AssignMemberManagePermissionRequest;
import com.hk.im.domain.request.group.DismissGroupRequest;
import com.hk.im.domain.request.group.EditMemberForbiddenStateRequest;
import com.hk.im.domain.request.group.HandoverMasterRequest;
import com.hk.im.domain.vo.GroupMemberVO;

import java.util.List;
import java.util.Set;

/**
 * @ClassName : GroupMemberService
 * @author : HK意境
 * @date : 2023/1/20 21:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface GroupMemberService extends IService<GroupMember> {

    /**
     * 移除群成员，踢出群员
     * @param request
     * @return
     */
    ResponseResult removeGroupMember(RemoveGroupMemberRequest request);

    /**
     * 邀请用户加群
     * @param request
     * @return
     */
    ResponseResult inviteGroupMember(InviteGroupMemberRequest request);

    /**
     * 跟新群聊成员备注名
     * @param request
     * @return
     */
    ResponseResult updateMemberRemarkName(MemberRemarkNameRequest request);

    /**
     * 获取群聊成员列表
     * @param groupId
     * @return
     */
    List<GroupMemberVO> getGroupMemberList(Long groupId);

    /**
     * 获取群聊成员Id集合
     * @param groupId
     * @return
     */
    List<GroupMember> getGroupMemberIdList(Long groupId);

    /**
     * 获取指定群聊指定群员
     * @param groupId
     * @param memberIdList
     * @return
     */
    List<GroupMember> getGroupMemberByIdList(Long groupId, List<Long> memberIdList);

    /**
     * 加入群聊
     * @param request
     * @return
     */
    ResponseResult joinGroup(JoinGroupRequest request);

    /**
     * 获取用户管理群聊
     * @param userId
     * @return
     */
    List<GroupMember> getMemberManageGroups(Long userId);

    /**
     * 获取用户可以邀请加入群聊的好友列表
     * @param userId
     * @param groupId
     * @return
     */
    ResponseResult getUserEnableInviteFriends(String userId, String groupId);


    /**
     * 是否群聊成员
     * @param groupId
     * @param friendId
     * @return
     */
    boolean isGroupMember(Long groupId,Long friendId);

    /**
     * 获取群聊指定聊群员
     * @param groupId
     * @param memberId
     * @return
     */
    GroupMember getTheGroupMember(Long groupId, Long memberId);

    /**
     * 获取用户加入群聊列表
     * @param userId
     * @return
     */
    List<GroupMember> getGroupMemberByUserId(Long userId);

    /**
     * 管理群员的管理员权限
     * @param request
     * @return
     */
    public ResponseResult manageGroupMemberPermission(AssignMemberManagePermissionRequest request);

    /**
     * 是否允许分配管理员权限
     * @param request
     * @return
     */
    public Boolean enableAssignManagePermission(AssignMemberManagePermissionRequest request);

    /**
     * 判断群员是否具有管理员权限
     * @param groupId
     * @param userId
     * @return
     */
    Boolean checkMemberHasManagePermission(Long groupId, Long userId);

    /**
     * 退出群聊
     * @param request
     * @return
     */
    ResponseResult quitTheGroup(QuitGroupRequest request);

    /**
     * 转让群主
     * @param request
     * @return
     */
    ResponseResult handoverGroupMaster(HandoverMasterRequest request);


    /**
     * 禁言群员
     * @param request
     * @return
     */
    ResponseResult editGroupMemberForbidden(EditMemberForbiddenStateRequest request);


    /**
     * 解散群聊
     * @param request
     * @return
     */
    ResponseResult dismissGroup(DismissGroupRequest request);
}
