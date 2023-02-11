package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.GroupConstants;
import com.hk.im.domain.constant.GroupMemberConstants;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.CreateGroupRequest;
import com.hk.im.domain.request.ModifyGroupInfoRequest;
import com.hk.im.domain.request.SetGroupAdministratorRequest;
import com.hk.im.infrastructure.mapper.GroupMapper;
import com.hk.im.service.service.GroupMemberService;
import com.hk.im.service.service.GroupService;
import com.hk.im.service.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : GroupServiceImpl
 * @date : 2023/1/21 21:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private UserService userService;

    /**
     * 创建群聊
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult createGroup(CreateGroupRequest request) {

        // 参数校验
        Long masterId = request.getMasterId();
        String groupName = request.getGroupName();
        if (Objects.isNull(masterId) || StringUtils.isEmpty(groupName)) {
            return ResponseResult.FAIL("请填写完整的群聊信息哦!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 初始成员
        if (CollectionUtils.isEmpty(request.getInitialGroupMembers())) {
            request.setInitialGroupMembers(new ArrayList<>());
        }

        // 准备数据
        GroupConstants.GroupCategory groupCategory = GroupConstants.getGroupCategory(request.getCategory());

        Group group = new Group();
        group.setGroupName(groupName)
                .setDescription(request.getDescription())
                .setGroupType(groupCategory.ordinal())
                .setMemberCount(request.getInitialGroupMembers().size());

        // 创建群聊
        boolean save = this.save(group);
        if (BooleanUtils.isFalse(save)) {
            // 创建群聊失败
            return ResponseResult.FAIL("创建群聊失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // 添加群初始成员
        ResponseResult res = this.addGroupMember(group.getId(), request.getInitialGroupMembers());

        // TODO 创建群聊成功，发布事件，消息

        // 构建返回结果：GroupVO
        return ResponseResult.SUCCESS(group).setMessage("创建群聊成功");
    }


    /**
     * 添加群成员
     *
     * @param id
     * @param groupMembers
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult addGroupMember(Long id, List<Long> groupMembers) {

        // 参数校验
        if (Objects.isNull(id) || CollectionUtils.isEmpty(groupMembers)) {
            return ResponseResult.FAIL("添加群员失败!");
        }
        Group group = this.getById(id);
        if (Objects.isNull(group)) {
            return ResponseResult.FAIL("群聊不存在!");
        }

        // 构造数据
        List<GroupMember> memberList = groupMembers.stream().map(userId -> {
            User user = this.userService.getById(userId);
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(group.getId())
                    .setGroupAccount(group.getGroupAccount())
                    .setGroupCategory(GroupConstants.GroupCategory.values()[group.getGroupType()].getCategory())
                    .setMemberId(userId)
                    .setMemberAvatar(user.getMiniAvatar())
                    .setMemberUsername(user.getUsername());
            return groupMember;
        }).toList();

        // 保存成员
        boolean saveBatch = this.groupMemberService.saveBatch(memberList);

        // 构造结果
        ResponseResult result = ResponseResult.FAIL("添加成员失败!");
        if (BooleanUtils.isTrue(saveBatch)) {
            result = ResponseResult.SUCCESS(memberList).setMessage("添加成员成功");
        }

        return result;
    }


    /**
     * 设置群管理员
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult setGroupAdministrator(SetGroupAdministratorRequest request) {

        // 校验参数
        String groupId = request.getGroupId();
        String memberId = request.getMemberId();
        Boolean set = request.getSet();

        if (StringUtils.isEmpty(groupId)) {
            return ResponseResult.FAIL("请选择正确的群聊!").setResultCode(ResultCode.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(memberId)) {
            return ResponseResult.FAIL("请选择正确的群员!").setResultCode(ResultCode.BAD_REQUEST);
        }
        if (Objects.isNull(set)) {
            return ResponseResult.FAIL("请选择为该群员执行的操作!!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 权限认证: 只有群主才能取消，设置群员为管理员
        Group group = this.getById(groupId);
        if (Objects.isNull(group)) {
            return ResponseResult.FAIL("请选择正确的群聊!").setResultCode(ResultCode.BAD_REQUEST);
        }

        // 是否为群主
        Long groupMaster = group.getGroupMaster();
        if (!StringUtils.equals(request.getMasterId(), String.valueOf(groupMaster))) {
            // 不是群主-> 无权操作
            return ResponseResult.FAIL("抱歉您没有权限执行此操作!").setResultCode(ResultCode.UNAUTHENTICATED);
        }

        // 是群主，群主不能设置、取消自己
        if (StringUtils.equals(String.valueOf(groupMaster), memberId)) {
            // 不能操作自己
            return ResponseResult.FAIL("抱歉您已经是群主呢!").setResultCode(ResultCode.NO_SUPPORT_OPERATION);
        }

        // 是否存在此群员
        GroupMember member = this.groupMemberService.getById(memberId);
        if (Objects.isNull(member)) {
            // 不存在此群员
            return ResponseResult.FAIL("抱歉此用户不是该群成员哦!").setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 根据操作进行设置
        ResponseResult result = null;
        if (BooleanUtils.isTrue(set)) {
            // 设置为管理员
            result = this.addGroupAdministrator(request);
        } else {
            // 取消管理员
            result = this.removeGroupAdministrator(request);
        }

        // TODO 更新管理员设置, 发布事件，消息，提送服务

        // 响应结果
        return result;
    }


    /**
     * 添加管理员
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult addGroupAdministrator(SetGroupAdministratorRequest request) {

        // 检查是否已经是管理员了
        String memberId = request.getMemberId();
        GroupMember member = this.groupMemberService.getById(memberId);

        // 角色
        Integer memberRole = member.getMemberRole();
        GroupMemberConstants.GroupMemberRole role = GroupMemberConstants.GroupMemberRole.values()[memberRole];
        // 已经是管理员或群主
        if (GroupMemberConstants.GroupMemberRole.ADMIN == role ||
                GroupMemberConstants.GroupMemberRole.MASTER == role) {
            return ResponseResult.FAIL("已经是群管理员呢!");
        }

        // 未知：非群员，等
        if (!Objects.equals(GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal(), memberRole)) {
            // 不是管理员，群主，普通群员
            return ResponseResult.FAIL("该用户身份未知!");
        }

        // 普通成员
        ResponseResult result = ResponseResult.FAIL("设置管理员失败!").setMessage("设置管理员失败!");
        // 设置为管理员
        member.setMemberRole(GroupMemberConstants.GroupMemberRole.ADMIN.ordinal());
        boolean update = this.groupMemberService.updateById(member);
        if (BooleanUtils.isTrue(update)) {
            result = ResponseResult.SUCCESS(member).setMessage("设置管理员成功!");
        }
        // TODO 更新管理员设置, 发布事件，消息，提送服务

        return result;
    }


    /**
     * 取消/移除管理员
     *
     * @param request
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult removeGroupAdministrator(SetGroupAdministratorRequest request) {

        // 是否存在该群员
        GroupMember groupMember = this.groupMemberService.getById(request.getMemberId());
        if (Objects.isNull(groupMember)) {
            return ResponseResult.FAIL("该用户不是群聊成员!").setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 是否已经是管理员
        if (!Objects.equals(groupMember.getMemberRole(), GroupMemberConstants.GroupMemberRole.ADMIN.ordinal())) {
            // 不是管理员
            return ResponseResult.FAIL("抱歉,该用户不是管理员!").setResultCode(ResultCode.NO_SUPPORT_OPERATION);
        }

        // 是否取消自己(群主)
        Group group = this.getById(request.getGroupId());
        if (Objects.equals(request.getMemberId(), String.valueOf(group.getGroupMaster()))) {
            // 操作群主自己：请通过转让群等操作，群主不支持取消自己得管理员
            return ResponseResult.FAIL("抱歉,您是群主不能取消自己的身份!").setResultCode(ResultCode.NO_SUPPORT_OPERATION);
        }

        // 取消管理员
        boolean update = this.groupMemberService.updateById(
                groupMember.setMemberRole(
                        GroupMemberConstants.GroupMemberRole.SIMPLE.ordinal()));

        // 构造响应数据
        if (BooleanUtils.isFalse(update)) {
            // 更新失败
            return ResponseResult.FAIL("取消管理员失败!").setResultCode(ResultCode.SERVER_BUSY);
        }

        // TODO 发布事件，消息

        return ResponseResult.SUCCESS(groupMember).setMessage("取消管理员成功!");
    }


    /**
     * 修改群聊信息
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateGroupInfo(ModifyGroupInfoRequest request) {

        Integer effected = this.groupMapper.updateGroupInfo(request);
        return null;
    }


    /**
     * 获取用户加入群组列表
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserJoinGroupList(String userId) {

        // 获取用户作为群员的的群列表
        List<GroupMember> memberList = this.groupMemberService.lambdaQuery()
                .eq(GroupMember::getMemberId, userId)
                .list();
        if (CollectionUtils.isEmpty(memberList)) {
            // 用户没有加入群
            memberList = Collections.emptyList();
        }
        // 加入的群聊id
        List<Long> groupIdList = memberList.stream().map(GroupMember::getGroupId).toList();
        // 查询群聊
        List<Group> groupList = this.listByIds(groupIdList);


        return null;
    }


}




