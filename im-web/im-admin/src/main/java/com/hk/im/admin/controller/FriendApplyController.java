package com.hk.im.admin.controller;

import com.hk.im.client.service.FriendApplyService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.FriendApply;
import com.hk.im.domain.request.ApplyHandleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : FriendApplyController
 * @date : 2023/1/2 19:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/apply")
public class FriendApplyController {

    @Resource
    private FriendApplyService friendApplyService;


    /**
     * 根据申请id 获取好友申请
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getFriendApplyById(@PathVariable(name = "id") Long id) {

        FriendApply apply = this.friendApplyService.getById(id);
        return ResponseResult.SUCCESS(apply);
    }


    /**
     * 获取用户的所有有效好友申请
     *
     * @param userId
     *
     * @return
     */
    @GetMapping("/user/send")
    public ResponseResult getUserAllSendApply(@RequestParam("userId") Long userId) {

        return this.friendApplyService.getUserAllSendFriendApply(userId);

    }


    /**
     * 获取用户所有收到的好友申请: 有效：待验证，已同意
     * @param userId
     * @return
     */
    @GetMapping("/user/receive")
    public ResponseResult getUserAllReceiveApply(@RequestParam("userId") String userId) {
        return this.friendApplyService.getUserAllReceiveFriendApply(Long.valueOf(userId));
    }


    @PostMapping("/handle/receive")
    public ResponseResult handleReceiveApply(@RequestBody ApplyHandleRequest request) {

        return this.friendApplyService.handleFriendApply(request);

    }



}
