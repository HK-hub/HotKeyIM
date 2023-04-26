package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.GroupMemberService;
import com.hk.im.client.service.GroupService;
import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.GroupMemberConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.*;
import com.hk.im.domain.request.group.AssignMemberManagePermissionRequest;
import com.hk.im.domain.vo.GroupMemberVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.infrastructure.event.group.event.JoinGroupEvent;
import com.hk.im.infrastructure.event.group.event.LeaveGroupEvent;
import com.hk.im.infrastructure.mapper.GroupMemberMapper;
import com.hk.im.infrastructure.mapstruct.GroupMemberMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
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
    @Resource
    private GroupService groupService;
    @Resource
    private FriendService friendService;
    @Resource
    private ApplicationContext applicationContext;

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
        Long groupId = request.getGroupId();
        Long operatorId = request.getOperatorId();
        List<Long> memberIdList = request.getMemberIdList();

        if (Objects.isNull(groupId) || Objects.isNull(operatorId) || CollectionUtils.isEmpty(memberIdList)) {
            // 请求参数不完整
            return ResponseResult.FAIL("移除群员失败!，操作信息不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 权限校验
        GroupMember operatorMember = this.getTheGroupMember(groupId, operatorId);
        if (Objects.isNull(operatorMember)) {
            // 操作者不是群员
            return ResponseResult.FAIL("操作者非该群成员!").setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 只有群主或管理员能可以踢人
        GroupMemberConstants.GroupMemberRole operatorRole = GroupMemberConstants.GroupMemberRole.values()[operatorMember.getMemberRole()];
        if (operatorRole != GroupMemberConstants.GroupMemberRole.ADMIN && operatorRole != GroupMemberConstants.GroupMemberRole.MASTER) {
            // 普通成员，无权
            return ResponseResult.FAIL("非常抱歉您不是管理员!").setResultCode(ResultCode.NO_PERMISSION);
        }

        // 收集将会被踢出的群员
        List<GroupMember> groupMemberByIdList = this.getGroupMemberByIdList(groupId, memberIdList);
        // 收集有权限踢出的群员
        List<GroupMember> canToRemoteMemberList = groupMemberByIdList.stream().filter(member -> {
            // 比较两者的角色权限
            GroupMemberConstants.GroupMemberRole memberRole = GroupMemberConstants.GroupMemberRole.values()[member.getMemberRole()];
            // admin = 2; master = 3: 只要群员角色 < 操作者角色值 就代表有权限
            return operatorRole.ordinal() > memberRole.ordinal();
        }).toList();

        // 踢出
        boolean remove = this.removeBatchByIds(canToRemoteMemberList);
        if (BooleanUtils.isFalse(remove)) {
            // 移除失败
            return ResponseResult.FAIL("移除成员失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发布事件，消息
        RemoveGroupMemberRequest leaveRequest = new RemoveGroupMemberRequest()
                .setGroupId(groupId)
                .setMemberList(canToRemoteMemberList)
                .setOperatorId(operatorId);
        this.applicationContext.publishEvent(new LeaveGroupEvent(this, leaveRequest));

        // 响应数据
        return ResponseResult.SUCCESS(canToRemoteMemberList.size()).setMessage("移除该成员成功!");
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
        List<String> inviteeIds = request.getInviteeIds();
        String groupId = request.getGroupId();
        String inviterId = request.getInviterId();

        // 参数不合法
        if (CollectionUtils.isEmpty(inviteeIds) || StringUtils.isEmpty(groupId) || StringUtils.isEmpty(inviterId)) {
            return ResponseResult.FAIL("请求参数不完整!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 邀请者是否本群成员
        GroupMember inviter = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, inviterId);
        if (Objects.isNull(inviter)) {
            // 邀请者不是本群群员，不能发起邀请用户
            return ResponseResult.FAIL("抱歉，您不是本群成员，无法邀请其他用户").setResultCode(ResultCode.NO_SUPPORT_OPERATION);
        }

        // 被邀请者是否已经成为群员
        for (String inviteeId : inviteeIds) {
            GroupMember invitee = this.groupMemberMapper.getGroupMemberByGroupIdAndMemberId(groupId, inviteeId);
            if (Objects.nonNull(invitee)) {
                // 被邀请者已经是群成员
                String message = "用户:" + invitee.getMemberUsername() + ",已经是群成员了，无需重复邀请!";
                return ResponseResult.FAIL(message).setResultCode(ResultCode.SERVER_BUSY);
            }
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
        List<User> inviteeUsers = this.userService.listByIds(inviteeIds);
        List<GroupMember> inviteeMemberList = inviteeUsers.stream().map(inviteeUser -> {
            GroupMember member = new GroupMember();
            member.setMemberAvatar(inviteeUser.getMiniAvatar())
                    .setMemberUsername(inviteeUser.getUsername())
                    .setMemberRemarkName(inviteeUser.getUsername())
                    .setMemberId(inviteeUser.getId())
                    .setGroupId(Long.valueOf(groupId))
                    .setMemberRole(GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal());
            return member;
        }).toList();

        // 批量拉入
        boolean save = this.saveBatch(inviteeMemberList);

        // 拉入群聊结果
        if (BooleanUtils.isFalse(save)) {
            // 拉人失败
            return ResponseResult.FAIL("邀请加入群聊失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 拉入群聊成功，发送消息，推送消息: XXX加入群聊

        return ResponseResult.SUCCESS("成功邀请用户加入群聊!");
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
     * 获取群聊成员ID列表
     * @param groupId
     * @return
     */
    @Override
    public List<GroupMember> getGroupMemberIdList(Long groupId) {

        List<GroupMember> memberIdList = this.lambdaQuery()
                .select(GroupMember::getMemberId)
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getStatus, 1)
                .list();

        return memberIdList;
    }


    /**
     * 获取指定群聊指定群员
     * @param groupId
     * @param memberIdList
     * @return
     */
    @Override
    public List<GroupMember> getGroupMemberByIdList(Long groupId, List<Long> memberIdList) {

        List<GroupMember> groupMemberList = this.lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .in(GroupMember::getMemberId, memberIdList)
                .eq(GroupMember::getStatus, 1)
                .list();
        return groupMemberList;
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
            return ResponseResult.FAIL("已经是群聊成员，无需重复加入!")
                    .setMessage("已经是群聊成员，无需重复加入!");
        }

        // 查询群聊
        Group group = this.groupService.getById(groupId);

        // 不是群员，加群
        User user = this.userService.getById(userId);
        member = new GroupMember()
                .setGroupId(Long.valueOf(groupId))
                .setGroupAccount(group.getGroupAccount())
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

        // TODO 加群成功，发送事件
        Long handlerId = UserContextHolder.get().getId();
        request.setHandlerId(String.valueOf(handlerId)).setOperation(1);
        this.applicationContext.publishEvent(new JoinGroupEvent(this, request));

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


    /**
     * 获取用户可以加入群聊的好友列表
     * @param userId
     * @param groupId
     * @return
     */
    @Override
    public ResponseResult getUserEnableInviteFriends(String userId, String groupId) {

        // 参数校验
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId)) {
            // 校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 判断群聊是否存在
        GroupVO groupVO = this.groupService.getGroupVOById(Long.valueOf(groupId));
        if (Objects.isNull(groupVO)) {
            // 群聊不存在，所有好友都可以邀请
            List<Friend> friendList = this.friendService.lambdaQuery()
                    .eq(Friend::getUserId, userId).list();
            // 转换
            /*List<FriendVO> friendVOList = friendList.stream()
                    .map(friend -> FriendMapStructure.INSTANCE.toVO(friend, null))
                    .toList();*/
            // 响应
            return ResponseResult.SUCCESS(friendList);
        }

        // 获取可以加入群聊好友列表
        List<Friend> friends = this.friendService.getAllEnableInviteFriends(groupId, userId);

        return ResponseResult.SUCCESS(friends);
    }


    /**
     * 是否是群聊成员
     * @param groupId
     * @param friendId
     * @return
     */
    @Override
    public boolean isGroupMember(Long groupId, Long friendId) {

        boolean exists = this.lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getMemberId, friendId)
                .exists();

        return exists;
    }


    /**
     * 获取群聊指定聊群员
     * @param groupId
     * @param memberId
     * @return
     */
    @Override
    public GroupMember getTheGroupMember(Long groupId, Long memberId) {

        GroupMember member = this.lambdaQuery()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getMemberId, memberId)
                .one();

        return member;
    }


    /**
     * 获取用户群聊列表
     * @param userId
     * @return
     */
    @Override
    public List<GroupMember> getGroupMemberByUserId(Long userId) {

        List<GroupMember> groupMemberList =  this.groupMemberMapper.selectGroupJoinedGroupList(userId);
        return groupMemberList;
    }


    /**
     * 分配或移除群员的管理权限
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult manageGroupMemberPermission(AssignMemberManagePermissionRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getGroupId()) || Objects.isNull(request.getPuppetId()) || Objects.isNull(request.getOperation());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL().setMessage("请填写完整的分配信息!");
        }

        // 素材
        Long groupId = request.getGroupId();
        Long handlerId = request.getHandlerId();
        Long puppetId = request.getPuppetId();
        Integer operation = request.getOperation();

        // 处理者是否有权限
        GroupMember handler = this.getTheGroupMember(groupId, handlerId);
        if (Objects.isNull(handler)) {
            // 不是本群人员
            return ResponseResult.FAIL().setMessage("抱歉你无权管理群聊管!");
        }

        // 查询权限
        GroupMemberConstants.GroupMemberRole handlerRole = GroupMemberConstants.GroupMemberRole.values()[handler.getMemberRole()];
        if (handlerRole == GroupMemberConstants.GroupMemberRole.DEFAULT
                || handlerRole == GroupMemberConstants.GroupMemberRole.SIMPLE) {
            // 无权
            return ResponseResult.FAIL().setMessage("抱歉你无权管理群聊管!");
        }

        // 查看被操纵者是否群员
        GroupMember groupMember = this.getTheGroupMember(groupId, puppetId);
        if (Objects.isNull(groupMember)) {
            // 不是本群成员
            return ResponseResult.FAIL().setMessage("该用户非群成员!");
        }

        // 获取现在傀儡的权限
        GroupMemberConstants.GroupMemberRole memberRole = GroupMemberConstants.GroupMemberRole.values()[groupMember.getMemberRole()];
        GroupMemberConstants.GroupMemberRole targetRole = GroupMemberConstants.GroupMemberRole.SIMPLE;
        // 操作者有权限，傀儡是群员：执行操作
        if (operation == 1) {
            // 分配管理员
            if (memberRole == GroupMemberConstants.GroupMemberRole.ADMIN || memberRole == GroupMemberConstants.GroupMemberRole.MASTER) {
                // 已经是管理员了
                return ResponseResult.SUCCESS().setMessage("已经具有管理员权限了,无需重复分配!");
            }

            // 查看管理员人数
            Boolean enableAssign = this.enableAssignManagePermission(request);
            // 设置为管理员
            targetRole = GroupMemberConstants.GroupMemberRole.ADMIN;
        } else if (operation == 0) {
            // 取消管理员权限
            if (memberRole != GroupMemberConstants.GroupMemberRole.ADMIN && memberRole != GroupMemberConstants.GroupMemberRole.MASTER) {
                // 非管理员
                return ResponseResult.SUCCESS().setMessage("该成员非管理员!");
            }
            // 取消管理员
            targetRole = GroupMemberConstants.GroupMemberRole.SIMPLE;
        }

        // 更新权限
        boolean update = this.lambdaUpdate()
                .eq(GroupMember::getId, groupMember.getId())
                .set(GroupMember::getMemberRole, targetRole.ordinal())
                .update();
        if (BooleanUtils.isFalse(update)) {
            return ResponseResult.FAIL().setMessage("操作失败!");
        }

        // 成功
        return ResponseResult.SUCCESS().setMessage("操作成功!");
    }


    /**
     * 是否允许分配管理员权限
     * @param request
     * @return
     */
    @Override
    public Boolean enableAssignManagePermission(AssignMemberManagePermissionRequest request) {

        // QQ 群管理员数量规则：200人->5管理员；500人->15管理员；1000人->15管理员；2000人->20管理员
        // 更多QQ群规则：https://kf.qq.com/faq/161223mI3eI3161223IvA3me.html

        // 统计群聊人数


        return null;
    }


    /**
     * 判断群员是否具有管理员权限
     * @param groupId
     * @param userId
     * @return
     */
    @Override
    public Boolean checkMemberHasManagePermission(Long groupId, Long userId) {

        // 获取群员
        GroupMember groupMember = this.getTheGroupMember(groupId, userId);
        if (Objects.isNull(groupMember)) {
            // 非本群成员
            return Boolean.FALSE;
        }

        // 判断权限
        // 查询权限
        GroupMemberConstants.GroupMemberRole handlerRole = GroupMemberConstants.GroupMemberRole.values()[groupMember.getMemberRole()];
        if (handlerRole == GroupMemberConstants.GroupMemberRole.ADMIN
                || handlerRole == GroupMemberConstants.GroupMemberRole.MASTER) {
            // 具有管理员权限
            return Boolean.TRUE;
        }

        // 无权
        return Boolean.FALSE;
    }


    /**
     * 退出群聊
     * @param request
     * @return
     */
    @Override
    public ResponseResult quitTheGroup(QuitGroupRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getGroupId()) || Objects.isNull(request.getMemberId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long memberId = request.getMemberId();
        Long groupId = request.getGroupId();

        // 获取群员
        GroupMember groupMember = this.getTheGroupMember(groupId, memberId);
        if (Objects.isNull(groupMember)) {
            // 群员不存在
            return ResponseResult.FAIL().setMessage("群员不存在!");
        }

        // 是否为群主
        Integer memberRole = groupMember.getMemberRole();
        if (GroupMemberConstants.GroupMemberRole.MASTER.ordinal() == memberRole) {
            // 群主不能退出群聊
            return ResponseResult.FAIL().setMessage("抱歉，群主退群须先转让群聊!");
        }

        // 退群
        boolean remove = this.removeById(groupMember);
        if (BooleanUtils.isFalse(remove)) {
            // 移除失败
            return ResponseResult.FAIL("退出群聊失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 退群成功，发布事件，消息
        RemoveGroupMemberRequest leaveRequest = new RemoveGroupMemberRequest()
                .setGroupId(groupId).setMemberIdList(List.of(memberId)).setMemberList(List.of(groupMember))
                .setOperatorId(memberId);
        this.applicationContext.publishEvent(new LeaveGroupEvent(this, leaveRequest));

        // 响应数据
        return ResponseResult.SUCCESS("退群群聊成功").setMessage("退群群聊成功!");
    }


}




