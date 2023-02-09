package com.hk.im.service.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.entity.FriendGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.FriendGroupRequest;

/**
 * @ClassName : FriendGroupService
 * @author : HK意境
 * @date : 2023/2/9 21:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
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

    /**
     * 创建分组
      * @param request
     * @return
     */
    ResponseResult createFriendGroup(FriendGroupRequest request);

    /**
     * 删除分组
     * @param request
     * @return
     */
    ResponseResult deleteFriendGroup(FriendGroupRequest request);

    /**
     * 修改分组：更新分组名
     * @param request
     * @return
     */
    ResponseResult renameFriendGroup(FriendGroupRequest request);

    /**
     * 移动好友到另一个分组
     * @param request
     * @return
     */
    ResponseResult moveFriendGroup(FriendGroupRequest request);

}
