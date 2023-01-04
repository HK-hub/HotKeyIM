package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.service.service.FriendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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



}
