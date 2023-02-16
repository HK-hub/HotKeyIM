package com.hk.im.service.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.FriendConstants;
import com.hk.im.domain.constant.GroupApplyConstants;
import com.hk.im.domain.constant.GroupConstants;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.domain.request.CreateGroupApplyRequest;
import com.hk.im.domain.request.JoinGroupRequest;
import com.hk.im.domain.vo.GroupApplyVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.event.friend.event.ApplyHandleEvent;
import com.hk.im.infrastructure.event.group.event.JoinGroupEvent;
import com.hk.im.infrastructure.mapper.GroupApplyMapper;
import com.hk.im.infrastructure.mapstruct.GroupApplyMapStructure;
import com.hk.im.infrastructure.mapstruct.GroupMapStructure;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.*;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : GroupApplyServiceImpl
 * @date : 2023/2/14 11:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class GroupApplyServiceImpl extends ServiceImpl<GroupApplyMapper, GroupApply> implements GroupApplyService {

    @Resource
    private GroupApplyMapper groupApplyMapper;
    @Resource
    private GroupService groupService;
    @Resource
    private GroupSettingService groupSettingService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private UserService userService;
    @Resource
    private ApplicationContext applicationContext;


    /**
     * 创建加群申请
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult createGroupApply(CreateGroupApplyRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getGroupId()) || StringUtils.isEmpty(request.getUserId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        Long groupId = Long.valueOf(request.getGroupId());
        Long userId = Long.valueOf(request.getUserId());

        // 群校验：
        Group group = this.groupService.getById(groupId);
        if (Objects.isNull(group)) {
            // 群聊不存在
            return ResponseResult.FAIL("抱歉, 该群聊不存在!");
        }

        // 群申请单
        GroupApply groupApply = new GroupApply()
                .setGroupId(groupId).setApplyInfo(request.getApplyInfo())
                .setApplyType(request.getJoinType()).setSenderId(userId);
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest().setUserId(String.valueOf(userId))
                .setGroupId(String.valueOf(groupId))
                .setApplyId(String.valueOf(groupApply.getId()))
                .setHandlerId(String.valueOf(group.getGroupMaster()))
                .setOperation(1);
        // 加群方式
        GroupSetting setting = this.groupSettingService.getById(groupId);
        GroupConstants.GroupJoinType joinType = GroupConstants.GroupJoinType.values()[setting.getJoinType()];
        // 判断加群方式
        if (joinType == GroupConstants.GroupJoinType.EVERYBODY) {
            // 任何人都可以加群
            boolean save = this.saveOrUpdate(groupApply);
            if (BooleanUtils.isFalse(save)) {
                // 加群失败
                return ResponseResult.FAIL("加群失败!");
            }

        } else if (joinType == GroupConstants.GroupJoinType.VERIFICATION) {
            // 需要管理员或群主验证
            boolean save = this.saveOrUpdate(groupApply);
            if (BooleanUtils.isFalse(save)) {
                // 加群失败
                return ResponseResult.FAIL("加群失败!");
            }
            // 待处理
            joinGroupRequest.setOperation(3);
            // TODO 发送事件通知，加群申请消息

            return ResponseResult.SUCCESS("加群申请已发送!");

        } else if (joinType == GroupConstants.GroupJoinType.ANSWER) {
            // 回答问题加群
            if (!StringUtils.equals(request.getApplyInfo(), setting.getAnswer())) {
                // 回答问题错误
                return ResponseResult.FAIL("加群问题答案错误!").setMessage("加群问题答案错误!");
            }
            // 加群问题答案正确 -> 加群成功

        } else if (joinType == GroupConstants.GroupJoinType.NOBODY) {
            // 不允许任何人加群
            return ResponseResult.FAIL("抱歉，该群不允许任何人加入!").setMessage("抱歉，该群不允许任何人加入!");
        }

        // 执行加群逻辑
        ResponseResult result = this.groupMemberService.joinGroup(joinGroupRequest);
        if (BooleanUtils.isFalse(result.isSuccess())) {
            // 加群失败
            return ResponseResult.FAIL("加群失败!");
        }
        // TODO 加群成功， 发送事件
        this.applicationContext.publishEvent(new JoinGroupEvent(this, joinGroupRequest));
        return ResponseResult.SUCCESS("加群成功!");
    }


    /**
     * 获取用户管理群聊的加群申请
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserGroupApplyList(String userId) {

        // 参数校验
        if (StringUtils.isEmpty(userId)) {
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 获取用户管理群聊
        List<GroupMember> memberManageGroups = this.groupMemberService.getMemberManageGroups(Long.valueOf(userId));
        // 收集群聊id
        Set<Long> groupIdSet = memberManageGroups.stream()
                .map(GroupMember::getGroupId).collect(Collectors.toSet());

        // 获取群聊申请
        List<GroupApply> groupApplyList = this.lambdaQuery()
                .eq(GroupApply::getStatus,
                        GroupApplyConstants.Status.PENDING.ordinal())
                .in(GroupApply::getGroupId, groupIdSet)
                .list();

        // 转换为 GroupApplyVO
        List<GroupApplyVO> groupApplyVOList = groupApplyList.stream()
                .map(apply -> {
                    // 查询群聊
                    Group group = this.groupService.getById(apply.getGroupId());
                    GroupVO groupVO = GroupMapStructure.INSTANCE.toVO(group, null, null, null);
                    // 查询发送者
                    User user = this.userService.getById(apply.getSenderId());
                    UserVO userVO = UserMapStructure.INSTANCE.toVo(user, null);
                    // 转换为 VO
                    return GroupApplyMapStructure.INSTANCE.toVO(apply, groupVO, userVO);
                }).toList();

        if (CollectionUtils.isEmpty(groupApplyVOList)) {
            groupApplyVOList = Collections.emptyList();
        }

        return ResponseResult.SUCCESS(groupApplyVOList);
    }


    /**
     * 处理群聊申请
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult handleGroupApply(ApplyHandleRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getApplyId()) || StringUtils.isEmpty(request.getHandlerId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setResultCode(ResultCode.BAD_REQUEST);
        }

        // 校验处理申请类型
        Integer type = request.getType();
        if (!Objects.equals(type, FriendConstants.FriendApplyType.GROUP.ordinal())) {
            // 不是处理群聊
            return ResponseResult.FAIL("申请处理类型不匹配!");
        }

        // 处理群聊申请
        String applyId = request.getApplyId();
        String handlerId = request.getHandlerId();
        Integer operation = request.getOperation();
        // 处理操作
        // 看接收者的申请操作
        FriendConstants.FriendApplyStatus operate =
                FriendConstants.FriendApplyStatus.values()[request.getOperation()];

        // 进行处理
        if (Objects.equals(operate, FriendConstants.FriendApplyStatus.REJECT)) {
            // 拒绝加群申请: 1.更新审批单状态
            boolean update = this.lambdaUpdate()
                    .set(GroupApply::getStatus, GroupApplyConstants.Status.REJECT.ordinal())
                    .eq(GroupApply::getId, applyId)
                    .update();
            if (BooleanUtils.isFalse(update)) {
                // 更新为拒绝状态
                return ResponseResult.FAIL("拒绝加群操作失败!");
            }
            // TODO 拒绝加群，发送事件
            this.applicationContext.publishEvent(new ApplyHandleEvent(this, request));
            // 响应
            return ResponseResult.SUCCESS("拒绝加群处理成功!");
        }

        // 执行加群操作
        // 1. 更新申请状态
        boolean update = this.lambdaUpdate()
                .set(GroupApply::getStatus, GroupApplyConstants.Status.AGREE.ordinal())
                .eq(GroupApply::getId, applyId)
                .update();
        if (BooleanUtils.isFalse(update)) {
            // 更新为拒绝状态
            return ResponseResult.FAIL("同意加群操作失败!");
        }

        // 2. 添加群员
        GroupApply groupApply = this.getById(applyId);
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest()
                .setApplyId(applyId)
                .setGroupId(String.valueOf(groupApply.getGroupId()))
                .setUserId(String.valueOf(groupApply.getSenderId()))
                .setHandlerId(request.getHandlerId())
                .setOperation(operation);
        // 加群
        ResponseResult joinResult = this.groupMemberService.joinGroup(joinGroupRequest);
        // 判断加群是否成功
        if (BooleanUtils.isFalse(joinResult.isSuccess())) {
            // 加群失败
            return ResponseResult.FAIL("同意加群失败!");
        }

        // TODO 加群成功，发送事件
        this.applicationContext.publishEvent(new ApplyHandleEvent(this, request));
        return ResponseResult.SUCCESS("同意加群操作成功!");
    }

}




