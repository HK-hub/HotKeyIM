package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.GroupMemberConstants;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.request.RemoveGroupMemberRequest;
import com.hk.im.infrastructure.mapper.GroupMemberMapper;
import com.hk.im.service.service.GroupMemberService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 *
 */
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService {


    /**
     * 踢出群聊
     * @param request
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
        GroupMember operatorMember = this.getById(operatorId);
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
        GroupMember groupMember = this.getById(memberId);
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
}




