package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.client.service.UserInfoService;
import com.hk.im.client.service.UserService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.constant.UserInfoConstants;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.domain.request.ChangeUserDetailRequest;
import com.hk.im.infrastructure.event.user.event.UserAvatarUpdateEvent;
import com.hk.im.infrastructure.mapper.UserInfoMapper;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private UserService userService;
    @Resource
    private AuthorizationService authorizationService;
    @Resource
    private ApplicationContext applicationContext;

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

    @Override
    public UserInfo getUserInfo(Long userId) {

        UserInfo one = this.lambdaQuery()
                .eq(UserInfo::getUserId, userId)
                .one();
        return one;
    }


    /**
     * 更新用户详细信息
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUserDetailInfo(ChangeUserDetailRequest request) {

        // 参数校验
        if (Objects.isNull(request)) {
            // 校验失败
            return ResponseResult.FAIL(ResultCode.BAD_REQUEST);
        }

        // 获取当前用户
        Long userId = request.getUserId();
        if (Objects.isNull(userId)) {
            userId = UserContextHolder.get().getId();
        }

        // 获取当前用户信息
        UserInfo userInfo = this.getById(userId);
        User user = this.userService.getById(userId);
        if (Objects.isNull(user)) {
            return ResponseResult.FAIL().setResultCode(ResultCode.NO_SUCH_USER);
        }

        // 更新
        {
            if (StringUtils.isNotEmpty(request.getUsername())) {
                // 昵称
                userInfo.setNickname(request.getUsername());
                user.setUsername(request.getUsername());
            }
            if (StringUtils.isNotEmpty(request.getAvatar())) {
                // 头像
                user.setMiniAvatar(request.getAvatar());
                user.setBigAvatar(request.getAvatar());
                // 发送修改用户信息事件
                this.applicationContext.publishEvent(new UserAvatarUpdateEvent(this, user));
            }
            if (StringUtils.isNotEmpty(request.getMotto())) {
                // 签名
                userInfo.setSignature(request.getMotto());
            }
            if (Objects.nonNull(request.getBirthday())) {
                // 出身日期校验: 出身日期不能晚于今天
                if (request.getBirthday().isAfter(LocalDate.now())) {
                    // 出身日期晚于今天
                    return ResponseResult.FAIL().setMessage("出生日期不能晚于今天!");
                }
                userInfo.setBirthday(request.getBirthday());
            }
            if (Objects.nonNull(request.getGender())) {
                int gender = UserInfoConstants.GenderEnum.values()[request.getGender()].ordinal();
                userInfo.setGender(gender);
            }
        }

        // 更新详细信息
        boolean infoUpdate = this.updateById(userInfo);
        boolean userUpdate = this.userService.updateUser(user);
        // 判断修改结果
        if (BooleanUtils.isFalse(infoUpdate) || BooleanUtils.isFalse(userUpdate)) {
            return ResponseResult.FAIL();
        }

        return ResponseResult.SUCCESS();
    }


}




