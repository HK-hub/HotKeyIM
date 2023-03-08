package com.hk.im.admin.controller;

import com.hk.im.client.service.UserInfoService;
import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.util.JWTUtils;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.request.ChangeUserDetailRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : UserInfoController
 * @date : 2023/1/7 19:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/info")
public class UserInfoController {

    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;

    @GetMapping
    public ResponseResult getUserInfo(HttpHeaders headers) {
        String token = headers.getFirst(JWTUtils.header);
        User user = UserContextHolder.get();
        ResponseResult result = userInfoService.getUserInfo(user.getId(), token);
        if (result.isSuccess()) {
            result = this.userService.getUserAndInfo(user.getId());
        }
        return result;

    }


    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    @GetMapping("/detail")
    public ResponseResult getUserDetails(@RequestParam("userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            // 获取登录用户
            userId = String.valueOf(UserContextHolder.get().getId());
        }
        return this.userService.getUserAndInfo(Long.valueOf(userId));
    }


    /**
     * 修改用户详细信息
     * @param request
     * @return
     */
    @PostMapping("/change/detail")
    public ResponseResult changeUserDetailInfo(@RequestBody ChangeUserDetailRequest request) {

        return this.userInfoService.updateUserDetailInfo(request);
    }


}
