package com.hk.im.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.ForgetAccountRequest;
import com.hk.im.domain.request.LoginOrRegisterRequest;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.vo.UserVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName : UserService
 * @author : HK意境
 * @date : 2022/12/30 17:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface UserService extends IService<User> {

    ResponseResult login(LoginOrRegisterRequest request);

    ResponseResult sendCode(String type, String account);

    ResponseResult register(LoginOrRegisterRequest request);

    ResponseResult updateUserAndInfo(UserVO userVO);

    ResponseResult getUserAndInfo(Long id);

    ResponseResult getUserAndInfoList(List<Long> idList);

    ResponseResult updateUserAvatar(InputStream inputStream, User user) throws IOException;

    // 找回密码或修改密码
    ResponseResult forgetOrUpdatePassword(ForgetAccountRequest request);

    // 退出登录
    ResponseResult logout(LoginOrRegisterRequest request);
}
