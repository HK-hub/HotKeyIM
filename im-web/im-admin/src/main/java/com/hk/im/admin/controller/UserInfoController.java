package com.hk.im.admin.controller;

import com.hk.im.admin.util.UserContextHolder;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.util.JWTUtils;
import com.hk.im.domain.entity.User;
import com.hk.im.service.service.UserInfoService;
import com.hk.im.service.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}
