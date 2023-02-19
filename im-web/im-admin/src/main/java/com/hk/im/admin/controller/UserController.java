package com.hk.im.admin.controller;

import com.hk.im.admin.util.UserContextHolder;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.AccountNumberGenerator;
import com.hk.im.common.util.NameUtil;
import com.hk.im.domain.entity.Friend;
import com.hk.im.domain.request.ForgetAccountRequest;
import com.hk.im.domain.request.LoginOrRegisterRequest;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.domain.request.SendCodeRequest;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.FriendService;
import com.hk.im.service.service.UserInfoService;
import com.hk.im.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private FriendService friendService;


    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable(name = "id") String id) {
        ResponseResult result = userService.getUserAndInfo(Long.valueOf(id));
        return result;
    }

    /**
     * 搜索用户
     * @param userId
     * @return
     */
    @GetMapping("/id")
    public ResponseResult searchUserById(@RequestParam("userId") String userId) {
        // 查询用户
        ResponseResult result = userService.getUserAndInfo(Long.valueOf(userId));
        if (BooleanUtils.isFalse(result.isSuccess())) {
            return result;
        }

        // 查询当前用登录用户与搜索用户之间关系
        User user = UserContextHolder.get();
        Friend relationship = this.friendService.isFriendRelationship(Long.valueOf(userId), user.getId());

        // 设置好友关系
        UserVO searchUser = (UserVO) result.getData();
        searchUser.setStatus(2);
        if (Objects.isNull(relationship)) {
            // 不是好友关系
            searchUser.setStatus(1);
        }
        return result;
    }


    /**
     * 发送验证码
     * @param request 验证码用途，1.登录注册，2.找回密码，3.修改密码
     * @return
     */
    @PostMapping("/code")
    public ResponseResult sendCode(@RequestBody SendCodeRequest request) {
        String account = request.getAccount();
        String type = request.getType();
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
        return result;
    }


    /**
     * 生成用户
     * @param request
     * @return
     */
    @PostMapping("/generate")
    public ResponseResult generate(@RequestBody LoginOrRegisterRequest request) {

        // 参数校验
        String nickname = request.getNickname();
        String password = request.getPassword();

        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(nickname)) {
            return ResponseResult.FAIL("用户名或密码不能为空哦!");
        }

        User user = new User();
        user.setAccount(AccountNumberGenerator.nextAccount());
        user.setUsername(NameUtil.getName());
        user.setPassword(BCrypt.hashpw("123456hh",BCrypt.gensalt()));
        user.setPhone(request.getPhone());
        // InternetSource.getInstance().randomEmail(10)
        user.setEmail(request.getEmail());

        // 保存用户
        this.userService.save(user);
        // 保存用户inxi
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNickname(user.getUsername());
        userInfoService.save(userInfo);

        return ResponseResult.SUCCESS(UserMapStructure.INSTANCE.toVo(user, userInfo));
    }


    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginOrRegisterRequest request) {

        ResponseResult result = userService.login(request);
        return result;
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(@RequestBody LoginOrRegisterRequest request) {

        ResponseResult result = userService.logout(request);
        return result;
    }

    /**
     * 忘记密码
     * @param request
     * @return
     */
    @PostMapping("/forget")
    public ResponseResult forget(@RequestBody ForgetAccountRequest request) {
        return this.userService.forgetOrUpdatePassword(request);
    }

    /**
     * 更新用户信息
     * @param userVO
     * @return
     */
    @PutMapping("/update/info")
    public ResponseResult modifyUserInfo(@RequestBody UserVO userVO) {

        ResponseResult result = this.userService.updateUserAndInfo(userVO);

        if (BooleanUtils.isFalse(result.isSuccess())) {
            // 更新失败
            result.setResultCode(ResultCode.FAIL).setData("更新用户信息失败!");
        }
        return result;
    }


    @PostMapping("/update/avatar")
    public ResponseResult updateUserAvatar(@RequestParam("avatar") MultipartFile file, @RequestParam("userId") Long userId) {

        // 获取User 对象
        User user = this.userService.getById(userId);
        ResponseResult result = new ResponseResult();
        try {
            result = this.userService.updateUserAvatar(file.getInputStream(), user);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result.setResultCode(ResultCode.SERVER_BUSY);
        }

    }




}
