package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.GroupMember;
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
}
