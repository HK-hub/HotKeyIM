package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.CreateGroupRequest;
import com.hk.im.service.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : GroupController
 * @date : 2023/1/2 16:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;


    /**
     * 创建群聊
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createGroup(@RequestBody CreateGroupRequest request) {
        return this.groupService.createGroup(request);
    }





}
