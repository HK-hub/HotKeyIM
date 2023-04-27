package com.hk.im.admin.controller;

import com.hk.im.client.service.FriendGroupService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.FriendGroupRequest;
import com.hk.im.domain.request.friend.EditFriendGroupListRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : FriendGroupController
 * @date : 2023/2/9 20:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/grouping")
public class FriendGroupController {

    @Resource
    private FriendGroupService friendGroupService;

    /**
     * 创建分组
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createFriendGroup(@RequestBody @NonNull FriendGroupRequest request) {
        return this.friendGroupService.createFriendGroup(request);
    }


    /**
     * 编辑好友分组
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public ResponseResult editFriendGroupList(@RequestBody @NonNull EditFriendGroupListRequest request) {

        return this.friendGroupService.editFriendGroupList(request);
    }

}
