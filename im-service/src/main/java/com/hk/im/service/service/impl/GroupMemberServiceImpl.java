package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.core.util.CollectionsUtil;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.GroupMemberConstants;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.InviteGroupMemberRequest;
import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.domain.request.MemberRemarkNameRequest;
import com.hk.im.domain.request.RemoveGroupMemberRequest;
import com.hk.im.domain.vo.GroupMemberVO;
import com.hk.im.infrastructure.mapper.GroupMemberMapper;
import com.hk.im.infrastructure.mapstruct.GroupMemberMapStructure;
import com.hk.im.service.service.GroupMemberService;
import com.hk.im.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : GroupMemberServiceImpl
 * @date : 2023/1/21 22:05
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {

    @Resource
    private UserService userService;
    @Resource
    private GroupMemberMapper groupMemberMapper;

    /**
     * 踢出群聊
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult removeGroupMember(RemoveGroupMemberRequest request) {
        // 参数校验
        String groupId = request.getGroupId();
        String operatorId = request.getOperatorId();
        String memberId = request.getMemberId();

        if (StringUtils.isEmpty(groupId) || StringUtils.isEmpty(operatorId) || StringUtils.isEmpty(memberId)) {
            // 请求参数不完整
            return ResponseResult.FAIL("移除群员失败!，操作信息不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 权限校验
        GroupMember operatorMember = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, operatorId);
        if (Objects.isNull(operatorMember)) {
            // 操作者不是群员
            return ResponseResult.FAIL("操作者非该群成员!").setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 只有群主或管理员能可以踢人
        if (Objects.equals(operatorMember.getMemberRole(), GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal())) {
            // 普通成员，无权
            return ResponseResult.FAIL("非常抱歉您不是管理员!").setResultCode(ResultCode.NO_PERMISSION);
        }

        // 被踢出者是否是群员
        GroupMember groupMember = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, operatorId);
        if (Objects.isNull(groupMember)) {
            return ResponseResult.FAIL("该成员不是群成员!").setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 踢出
        boolean remove = this.removeById(groupMember);
        if (BooleanUtils.isFalse(remove)) {
            // 移除失败
            return ResponseResult.FAIL("移除该成员失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发布事件，消息

        // 响应数据
        return ResponseResult.SUCCESS(groupMember).setMessage("移除该成员成功!");
    }


    /**
     * 邀请用户加群
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult inviteGroupMember(InviteGroupMemberRequest request) {

        // 参数校验
        String inviteeId = request.getInviteeId();
        String groupId = request.getGroupId();
        String inviterId = request.getInviterId();

        // 参数不合法
        if (StringUtils.isEmpty(inviteeId) || StringUtils.isEmpty(groupId) || StringUtils.isEmpty(inviterId)) {
            return ResponseResult.FAIL("请求参数不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 邀请者是否本群成员
        GroupMember inviter = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, inviterId);
        if (Objects.isNull(inviter)) {
            // 邀请者不是本群群员，不能发起邀请用户
            return ResponseResult.FAIL("抱歉，您不是本群成员，无法邀请其他用户").setResultCode(ResultCode.NO_SUPPORT_OPERATION);
        }

        // 被邀请者是否已经成为群员
        GroupMember invitee = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, inviteeId);
        if (Objects.nonNull(invitee)) {
            // 被邀请者已经是群成员
            return ResponseResult.FAIL("该用户已经是群成员了，无需重复邀请!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // 是否有权限：按照群聊的加群方式，审核方式确定
        Integer role = inviter.getMemberRole();
        GroupMemberConstants.GroupMemberRole groupMemberRole = GroupMemberConstants.GroupMemberRole.values()[role];

        // 2. 普通成员邀请：如果加群审核方式为无需审核，则直接拉入群聊；如果需要审核则进行审核
        // 1. 群主邀请，管理员邀请-> 直接将人拉入群聊
        if (Objects.equals(groupMemberRole, GroupMemberConstants.GroupMemberRole.SIMPLE) ||
                Objects.equals(groupMemberRole, GroupMemberConstants.GroupMemberRole.DEFAULT)) {
            // TODO 普通用户：需要根据加群规则确定

            // TODO 推送消息: 推送审核消息
        }

        // 拉入群聊
        User inviteeUser = this.userService.getById(inviteeId);
        GroupMember member = new GroupMember();
        member.setMemberAvatar(inviteeUser.getMiniAvatar())
                .setMemberUsername(inviteeUser.getUsername())
                .setMemberId(Long.valueOf(inviteeId))
                .setGroupId(Long.valueOf(groupId))
                .setMemberRole(GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal());
        boolean save = this.save(member);

        // 拉入群聊结果
        if (BooleanUtils.isFalse(save)) {
            // 拉人失败
            return ResponseResult.FAIL("邀请加入群聊失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 拉入群聊成功，发送消息，推送消息: XXX加入群聊

        return ResponseResult.SUCCESS("成功邀请该用户加入群聊!");
    }


    /**
     * 更新群员备注名
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult updateMemberRemarkName(MemberRemarkNameRequest request) {

        // 参数校验
        String groupId = request.getGroupId();
        String memberId = request.getMemberId();
        String operatorId = request.getOperatorId();
        String newRemarkName = request.getNewRemarkName();
        if (StringUtils.isEmpty(groupId) || StringUtils.isEmpty(memberId)
                || StringUtils.isEmpty(operatorId) || StringUtils.isEmpty(newRemarkName)) {
            // 请求参数不完整
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 群员和操作者是否群员
        GroupMember member = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, operatorId);
        if (Objects.isNull(member)) {
            return ResponseResult.FAIL("该用户不是群员!");
        }
        // 操作者：只能为群主，管理员，自己。其他人不能修改他人昵称
        GroupMember operator = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId,operatorId);
        if (Objects.isNull(operator)) {
            // 操作者不是群员
            return ResponseResult.FAIL("抱歉您不是该群成员!");
        }

        // 是否有权限: 1.群主可以修改任何人的昵称；2.管理员可以修改普通成员的昵称；3.普通成员只能修改自己的昵称
        GroupMemberConstants.GroupMemberRole operatorRole = GroupMemberConstants.GroupMemberRole.values()[operator.getMemberRole()];
        GroupMemberConstants.GroupMemberRole memberRole = GroupMemberConstants.GroupMemberRole.values()[member.getMemberRole()];

        // 如果 operatorRole < memberRole 则无权限操作
        if (operatorRole.ordinal() < memberRole.ordinal()) {
            return ResponseResult.FAIL("抱歉您无权修改该群员群昵称!");
        }

        // 修改群昵称
        boolean update = this.lambdaUpdate().eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getMemberId, memberId)
                .set(GroupMember::getMemberRemarkName, newRemarkName)
                .update();

        // 封装结果数据
        if (BooleanUtils.isFalse(update)) {
            return ResponseResult.FAIL("修改群员群昵称失败!");
        }

        // TODO 推送消息，发布事件

        return ResponseResult.SUCCESS("修改群员群昵称成功!");
    }

    /**
     * 获取群聊成员列表
     * @param groupId
     * @return
     */
    @Override
    public List<GroupMemberVO> getGroupMemberList(Long groupId) {

        List<GroupMember> groupMemberList = this.groupMemberMapper.selectGroupMemberList(groupId);
        if (CollectionUtils.isEmpty(groupMemberList)) {
            groupMemberList = Collections.emptyList();
        }
        // 转换
        List<GroupMemberVO> memberVOList = groupMemberList.stream()
                .map(GroupMemberMapStructure.INSTANCE::toVO)
                .toList();
        return memberVOList;
    }

    /**
     * 无条件加入群聊
     * @param request
     * @return
     */
    @Override
    public ResponseResult joinGroup(JoinGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getUserId()) || StringUtils.isEmpty(request.getGroupId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }
        String groupId = request.getGroupId();
        String userId = request.getUserId();

        // 查看是否已经加入群聊
        GroupMember member = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, userId);
        if (Objects.nonNull(member)) {
            // 已经加入群聊
            return ResponseResult.FAIL("你已经是群聊成员，无需重复加入!")
                    .setMessage("你已经是群聊成员，无需重复加入!");
        }

        // 不是群员，加群
        User user = this.userService.getById(userId);
        member = new GroupMember()
                .setGroupId(Long.valueOf(groupId))
                .setMemberId(Long.valueOf(userId))
                .setMemberUsername(user.getUsername())
                .setMemberAvatar(user.getMiniAvatar())
                .setMemberRole(GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal())
                .setMemberRemarkName(user.getUsername());
        // 加群
        boolean save = this.save(member);
        if (BooleanUtils.isFalse(save)) {
            return ResponseResult.FAIL("加群失败");
        }
        return ResponseResult.SUCCESS("加入群聊成功!");
    }


    /**
     * 获取用户管理群聊
     * @param userId
     * @return
     */
    @Override
    public List<GroupMember> getMemberManageGroups(Long userId) {

        List<GroupMember> groupMemberList =  this.groupMemberMapper.selectGroupManageGroupList(userId);
        if (CollectionUtils.isEmpty(groupMemberList)) {
            groupMemberList = Collections.emptyList();
        }
        return groupMemberList;
    }
}




