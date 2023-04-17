package com.hk.im.admin.controller;

import com.hk.im.client.service.FindService;
import com.hk.im.client.service.FriendApplyService;
import com.hk.im.client.service.FriendService;
import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.PageResult;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.request.FriendApplyRequest;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.request.UserFindPolicyRequest;
import com.hk.im.domain.response.FriendSearchResponse;
import com.hk.im.domain.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : FindController
 * @date : 2023/1/2 13:46
 * @description : 好友发现，好友添加
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/find")
public class FindController {

    @Resource
    private UserService service;
    @Resource
    private FriendService friendService;
    @Resource
    private FriendApplyService friendApplyService;
    @Resource
    private FindService findService;


    /**
     * 好友，群聊推荐
     * @return
     */
    @GetMapping("/recommend")
    public ResponseResult recommendFriendAndGroup() {
        return ResponseResult.FAIL("新功能请等待!");
    }


    /**
     * 账户搜索用户，只返回精确匹配的一个
     * @param request
     * @return
     */
    @PostMapping("/friend/account")
    public ResponseResult findFriendAccountOrName(@RequestBody FriendFindRequest request) {

        log.info("request={}", request);
        PageResult pageResult = this.friendApplyService.findFriendAccountOrName(request);
        if (CollectionUtils.isEmpty(pageResult.getRows())) {
            return ResponseResult.FAIL("没有找到符合搜索条件的用户或对方设置了账号搜索限制!");
        }
        UserVO userVO = (UserVO) pageResult.getRows().get(0);
        FriendSearchResponse response = new FriendSearchResponse().setUser(userVO).setStatus(1);
        Friend relationship = this.friendService.isFriendRelationship(Long.valueOf(request.getOperatorId()), userVO.getId());
        if (Objects.nonNull(relationship)) {
            // 不是好友关系
            response.setStatus(2);
        }
        return ResponseResult.SUCCESS(response);
    }


    /**
     * 关键字搜索发现好友：返回好友集合
     * @param request
     * @return
     */
    @PostMapping("/friend/search")
    public ResponseResult searchFriend(@RequestBody FriendFindRequest request) {

        log.info("request={}", request);
        PageResult pageResult = this.friendApplyService.findFriendAccountOrName(request);
        if (CollectionUtils.isEmpty(pageResult.getRows())) {
            return ResponseResult.FAIL("没有找到符合搜索条件的用户或对方设置了账号搜索限制!");
        }

        return ResponseResult.SUCCESS(pageResult);
    }

    /**
     * 群发现
     * @param request
     * @return
     */
    @GetMapping("/group/account")
    public ResponseResult findGroupAccountOrName(@RequestBody FriendFindRequest request) {
        // 分页查询
        PageResult pageResult = this.friendApplyService.findGroupAccountOrName(request);
        if (CollectionUtils.isEmpty(pageResult.getRows())) {
            return ResponseResult.FAIL("没有找到符合搜索条件的群聊或对方设置了账号搜索限制!");
        }
        return ResponseResult.SUCCESS(pageResult);
    }


    /**
     * 好友申请
     * @param request
     * @return
     */
    @PostMapping("/friend/apply")
    public ResponseResult friendApply(@RequestBody FriendApplyRequest request) {
        return this.friendApplyService.applyToBeFriend(request);
    }


    /**
     * 查询群聊服务
     * @param request
     * @return
     */
    @GetMapping("/group/search")
    public ResponseResult searchGroup(@RequestBody FriendFindRequest request) {
        return this.findService.searchForGroup(request);
    }


    /**
     * 公开群组
     * @param request
     * @return
     */
    @GetMapping("/group/public")
    public ResponseResult publicGroup(@RequestBody UserFindPolicyRequest request) {
        return this.findService.getPublicGroups(request);
    }


}
