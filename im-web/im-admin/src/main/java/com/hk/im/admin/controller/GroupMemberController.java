package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.InviteGroupMemberRequest;
import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.domain.request.MemberRemarkNameRequest;
import com.hk.im.domain.request.RemoveGroupMemberRequest;
import com.hk.im.domain.vo.GroupMemberVO;
import com.hk.im.service.service.GroupMemberService;
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
    @DeleteMapping("/remove")
    public ResponseResult removeGroupMember(@NotNull @RequestBody RemoveGroupMemberRequest request) {

        return this.groupMemberService.removeGroupMember(request);
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

}
