package com.hk.im.admin.controller;

import com.hk.im.admin.util.UserContextHolder;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.ModifyFriendInfoRequest;
import com.hk.im.domain.request.ModifyFriendStatusRequest;
import com.hk.im.service.service.FriendService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.http.HttpRequest;

/**
 * @author : HK意境
 * @ClassName : FriendController
 * @date : 2023/1/3 17:39
 * @description : 好友控制器
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private FriendService friendService;


    /**
     * 获取用户的好友列表
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getUserFriendList(@RequestParam("userId") Long userId) {

        return this.friendService.getUserFriendList(userId);
    }

    /**
     * 获取用户的好友列表V2版本
     * @param userId
     * @return
     */
    @GetMapping("/list/v2")
    public ResponseResult getUserFriendListV2(@RequestParam("userId") Long userId) {
        return this.friendService.getUserFriendListV2(userId);
    }

    /**
     * 修改好友信息：备注，备注信息，分组
     * @return
     */
    @PostMapping("/modify/remark")
    public ResponseResult modifyFriendInfo(@RequestBody ModifyFriendInfoRequest request) {
        return this.friendService.updateFriendInfo(request);
    }


    /**
     * 修改好友分组信息
     * @param request
     * @return
     */
    @PostMapping("/modify/group")
    public ResponseResult modifyFriendGroup(@RequestBody ModifyFriendInfoRequest request) {
        return this.friendService.updateFriendInfo(request);
    }

    /**
     * 删除好友
     * @param friendId
     * @return
     */
    @DeleteMapping("/remove")
    public ResponseResult removeFriend(@RequestParam("userId") String friendId) {
        // 获取当前用户
        User user = UserContextHolder.get();
        ResponseResult result = null;
        try {
            result = this.friendService.removeFriend(friendId, user);
        } catch (ApiException e) {
            result = ResponseResult.FAIL(e.getMessage()).setMessage("删除好友失败!");
        }
        return result;
    }


    /**
     * 修改好友状态
     * @param request
     * @return
     */
    @PostMapping("/modify/state")
    public ResponseResult modifyFriendState(@RequestBody ModifyFriendStatusRequest request) {
        return this.friendService.updateFriendStatus(request);
    }


}
