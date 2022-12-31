package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.dto.LoginOrRegisterRequest;
import com.hk.im.domain.dto.UserDTO;
import com.hk.im.domain.entity.User;
import com.hk.im.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : UserController
 * @date : 2022/12/30 15:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public String echo() {
        return "hello world";
    }

    @GetMapping("/{id}")
    public ResponseResult<User> getUserById(@PathVariable(name = "id")String id) {
        User user = userService.getById(id);
        if (Objects.isNull(user)) {
            user = new User();
            user.setId(111111L);
            user.setUsername("HK意境");
        }

        return ResponseResult.SUCCESS(user);
    }

    /**
     * 发送验证码
     * @param type：验证码用途，1.登录注册，2.找回密码，3.修改密码
     * @return
     */
    @PostMapping("/code/{type}")
    public ResponseResult sendCode(@PathVariable(name = "type", required = false) String type, String account) {
        log.info("parameter:{},{}", type, account);
        return this.userService.sendCode(type, account);
    }


    /**
     * 注册用户
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody LoginOrRegisterRequest request) {
        ResponseResult result = this.userService.register(request);
        if (BooleanUtils.isTrue(result.isSuccess())) {
            // 注册成功
            result.setMessage("注册用户成功!");
        }
        return result;
    }


    @PostMapping("/login")
    public ResponseResult<UserDTO> login(@RequestBody LoginOrRegisterRequest request) {

        ResponseResult result = userService.login(request);
        return result;
    }







}
