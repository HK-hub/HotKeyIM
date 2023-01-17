package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.GroupConstants;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.CreateGroupRequest;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
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
     * @param request
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
     * @param id
     * @param groupMembers
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
}




