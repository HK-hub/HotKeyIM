package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.domain.request.InviteGroupMemberRequest;
import com.hk.im.domain.request.MemberRemarkNameRequest;
import com.hk.im.domain.request.RemoveGroupMemberRequest;

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
}
