package com.hk.im.service.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.FriendGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface FriendGroupService extends IService<FriendGroup> {

    /**
     * 获取用户默认分组
     * @param userId
     * @return
     */
    ResponseResult getUserDefaultGroup(Long userId);

    /**
     * 获取用户所有分组
     * @param userId
     * @return
     */
    ResponseResult getUserAllGroup(Long userId);
}
