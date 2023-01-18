package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.CreateGroupRequest;
import com.hk.im.domain.request.SetGroupAdministratorRequest;
import com.hk.im.service.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
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


    /**
     * 设置群管理员, 取消群管理员
     * @param request
     * @return
     */
    @PostMapping("/set/admin")
    public ResponseResult setGroupAdministrator(@RequestBody SetGroupAdministratorRequest request) {

        return this.groupService.setGroupAdministrator(request);

    }




}
