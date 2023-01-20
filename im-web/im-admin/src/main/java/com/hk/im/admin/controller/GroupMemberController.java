package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.RemoveGroupMemberRequest;
import com.hk.im.service.service.GroupMemberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author : HK意境
 * @ClassName : GroupMemberController
 * @date : 2023/1/18 22:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/member")
public class GroupMemberController {

    @Resource
    private GroupMemberService groupMemberService;


    /**
     * 移除群成员/ 踢出群聊
     * @param request
     * @return
     */
    @DeleteMapping("/remove")
    public ResponseResult removeGroupMember(@NotNull @RequestBody RemoveGroupMemberRequest request) {

        return this.groupMemberService.removeGroupMember(request);
    }



}
