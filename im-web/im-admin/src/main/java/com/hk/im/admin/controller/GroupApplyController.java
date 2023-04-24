package com.hk.im.admin.controller;

import com.hk.im.client.service.GroupApplyService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.ApplyHandleRequest;
import com.hk.im.domain.request.CreateGroupApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : GroupApplyController
 * @date : 2023/2/14 11:32
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/group/apply")
public class GroupApplyController {

    @Resource
    private GroupApplyService groupApplyService;

    /**
     * 申请加入群聊
     *
     * @param request
     *
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createGroupApply(@RequestBody @NonNull CreateGroupApplyRequest request) {
        return this.groupApplyService.createGroupApply(request);
    }

    /**
     * 获取群聊加群申请方式设置
     * @param groupId
     * @return
     */
    @GetMapping("/setting")
    public ResponseResult getGroupApplySetting(@RequestParam("groupId") Long groupId) {

        return this.groupApplyService.getGroupApplySetting(groupId);
    }

    /**
     * 获取用户管理群聊加群申请单
     *
     * @param userId
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getGroupApplyList(@RequestParam("userId") @NonNull String userId) {
        return this.groupApplyService.getUserGroupApplyList(userId);
    }


    /**
     * 处理群聊申请
     * @param request
     * @return
     */
    @PostMapping("/handle")
    public ResponseResult handleGroupApply(@RequestBody ApplyHandleRequest request) {
        return this.groupApplyService.handleGroupApply(request);

    }


}



