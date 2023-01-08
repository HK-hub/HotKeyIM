package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import com.hk.im.service.service.AuthorizationService;
import com.hk.im.service.service.UserInfoService;
import com.hk.im.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName : UserInfoServiceImpl
 * @author : HK意境
 * @date : 2023/1/7 19:20
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private AuthorizationService authorizationService;

    @Override
    public ResponseResult getUserInfo(Long userId, String token) {

        // 获取当前用户
        if (Objects.isNull(userId) || StringUtils.isEmpty(token)) {
            // 当前用户不存在, 未登录
            return ResponseResult.FAIL(ResultCode.TOKEN_INVALIDATE).setMessage("抱歉你还未登录!");
        }

        // 校验 token 是否合法
        User user = authorizationService.authUserByToken(token);
        if (Objects.isNull(user) || !Objects.equals(userId, user.getId())) {
            // token 校验失败或和当前用户不匹配
            return ResponseResult.FAIL(ResultCode.TOKEN_INVALIDATE).setMessage("抱歉您的登录过期了，请重新登录!");
        }

        return ResponseResult.SUCCESS(userId);
    }
}




