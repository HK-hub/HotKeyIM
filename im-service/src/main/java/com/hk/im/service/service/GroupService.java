package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.request.CreateGroupRequest;

import java.util.List;

/**
 * @ClassName : GroupService
 * @author : HK意境
 * @date : 2023/1/17 12:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface GroupService extends IService<Group> {

    /**
     * 创建群聊
     * @param request
     * @return
     */
    ResponseResult createGroup(CreateGroupRequest request);

    /**
     * 添加群成员
     * @param id
     * @param groupMembers
     * @return
     */
    ResponseResult addGroupMember(Long id, List<Long> groupMembers);
}
