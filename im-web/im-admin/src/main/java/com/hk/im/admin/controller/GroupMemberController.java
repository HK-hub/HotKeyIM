package com.hk.im.admin.controller;

import com.hk.im.client.service.GroupMemberService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.group.AssignMemberManagePermissionRequest;
import com.hk.im.domain.request.group.DismissGroupRequest;
import com.hk.im.domain.request.group.EditMemberForbiddenStateRequest;
import com.hk.im.domain.request.group.HandoverMasterRequest;
import com.hk.im.domain.vo.GroupMemberVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : GroupMemberController
 * @date : 2023/1/18 22:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/member")
public class GroupMemberController {

    @Resource
    private GroupMemberService groupMemberService;


    @PostMapping("/join")
    public ResponseResult joinGroup(@RequestBody JoinGroupRequest request) {
        return this.groupMemberService.joinGroup(request);
    }

    /**
     * 移除群成员/ 踢出群聊
     * @param request
     * @return
     */
    @PostMapping("/remove")
    public ResponseResult removeGroupMember(@RequestBody RemoveGroupMemberRequest request) {

        return this.groupMemberService.removeGroupMember(request);
    }


    /**
     * 退出群聊
     * @param request
     * @return
     */
    @PostMapping("/quit")
    public ResponseResult quitGroup(@RequestBody QuitGroupRequest request) {

        return this.groupMemberService.quitTheGroup(request);
    }


    /**
     * 解散群聊
     * @param request
     * @return
     */
    @PostMapping("/")
    public ResponseResult dismissGroup(@RequestBody DismissGroupRequest request) {


        return this.groupMemberService.dismissGroup(request);
    }

    /**
     * 邀请用户加群
     * @param request
     * @return
     */
    @PostMapping("/invite")
    public ResponseResult inviteGroupMember(@NotNull @RequestBody InviteGroupMemberRequest request) {

        return this.groupMemberService.inviteGroupMember(request);
    }


    /**
     * 修改群里成员备注名称
     * @param request
     * @return
     */
    @PostMapping("/remark")
    public ResponseResult modifyMemberRemarkName(@NotNull @RequestBody MemberRemarkNameRequest request) {
        return this.groupMemberService.updateMemberRemarkName(request);
    }


    /**
     * 获取用户可以邀请进入群聊的好友列表
     * @param userId
     * @param groupId
     * @return
     */
    @GetMapping("/enable/invite")
    public ResponseResult enableInviteFriends(String userId, String groupId) {
        return this.groupMemberService.getUserEnableInviteFriends(userId, groupId);
    }


    /**
     * 获取群聊群成员列表
     * @param groupId
     * @return
     */
    @GetMapping("/group/list")
    public ResponseResult getGroupMemberList(@RequestParam String groupId) {

        List<GroupMemberVO> groupMemberList = this.groupMemberService.getGroupMemberList(Long.valueOf(groupId));
        return ResponseResult.SUCCESS(groupMemberList);
    }


    /**
     * 分配和管理 成员的管理员权限
     * @param request
     * @return
     */
    @PostMapping("/manage/assign")
    public ResponseResult assignGroupMemberManagePermission(@RequestBody AssignMemberManagePermissionRequest request) {

        return this.groupMemberService.manageGroupMemberPermission(request);
    }


    /**
     * 转让群主
     * @param request
     * @return
     */
    @PostMapping("/master/handover")
    public ResponseResult handoverGroupMaster(@RequestBody HandoverMasterRequest request) {

        return this.groupMemberService.handoverGroupMaster(request);
    }


    /**
     * 禁言群员
     * @param request
     * @return
     */
    @PostMapping("/forbidden/mute/state")
    public ResponseResult editGroupMemberForbidden(@RequestBody EditMemberForbiddenStateRequest request) {

        return this.groupMemberService.editGroupMemberForbidden(request);
    }




}
