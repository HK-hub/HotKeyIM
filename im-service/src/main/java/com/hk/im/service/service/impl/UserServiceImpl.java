package com.hk.im.service.service.impl;

import com.apifan.common.random.source.NumberSource;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.*;
import com.hk.im.domain.request.ForgetAccountRequest;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.request.LoginOrRegisterRequest;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.infrastructure.event.user.event.UserUpdatedEvent;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : UserServiceImpl
 * @date : 2022/12/30 17:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserManager userManager;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private MinioService minioService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AuthorizationService authorizationService;
    @Resource
    private MailService mailService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 发送验证码
     *
     * @param type    默认为登录注册
     * @param account
     *
     * @return
     */
    @Override
    public ResponseResult sendCode(String type, String account) {

        if (Objects.isNull(type)) {
            // 默认为登录注册验证码
            type = UserConstants.LOGIN_OR_REGISTER;
        }

        // 校验参数
        boolean invalidMobile = CustomValidator.invalidMobile(account);
        boolean invalidEmail = CustomValidator.invalidEmail(account);

        if (invalidEmail && invalidMobile) {
            // 非法邮箱 非法手机号 -> 为账号类型
            return ResponseResult.FAIL("请输入正确的手机号或邮箱");
        }

        String code = null;
        // 如果是邮箱
        if (BooleanUtils.isTrue(!invalidEmail)) {
            code = String.valueOf(NumberSource.getInstance().randomInt(1234, 9999));
            String subject = null;
            // 确定主题
            if (Objects.equals(type, UserConstants.FIND_PASSWORD)) {
                subject = "Hot Key IM聊天应用-找回账号验证码";
            } else if (Objects.equals(type, UserConstants.LOGIN_OR_REGISTER)) {
                subject = "Hot Key IM聊天应用-登陆注册验证码";
            } else {
                // 非法类型
                return ResponseResult.FAIL("对不起，您的操作非法!");
            }
            String content = "<h2>您的登录注册验证码如下：" + code + "</h2>";
            mailService.sendSimpleMail("3161880795@qq.com", "Hot Key IM 官方", account,
                    null, subject, content);

            // 保存码
            authorizationService.createAuthCode(type, account, code);
            System.out.println("您的验证码已经发送了：" + code);
        } else {
            // TODO
            // 发送手机号码
            return ResponseResult.FAIL("暂不支持此操作!");
        }

        return ResponseResult.SUCCESS("您的验证码已经发送了!");
    }


    /**
     * 用户登录
     *
     * @param loginRequest
     *
     * @return
     */
    @Override
    public ResponseResult login(LoginOrRegisterRequest loginRequest) {

        Integer type = loginRequest.getType();

        User user = null;
        // 密码登录
        if (Objects.equals(type, LoginOrRegisterRequest.LoginType.PASSWORD.ordinal())) {

            // 查询用户
            user = this.userManager.findUserByAccountOrPhoneOrEmail(loginRequest.getAccount());
            if (Objects.isNull(user)) {
                // 用户不存在
                return ResponseResult.FAIL("该账号不存在或未绑定相关账号!");
            }

            String password = loginRequest.getPassword();
            boolean checkpw = BCrypt.checkpw(password, user.getPassword());

            // 登录结果验证
            if (BooleanUtils.isFalse(checkpw)) {
                // 登录失败
                return ResponseResult.FAIL("账号或手机或邮箱或密码错误");
            }

        } else if (Objects.equals(type, LoginOrRegisterRequest.LoginType.CODE.ordinal())) {
            // 验证码登录
            // 参数校验
            String phone = loginRequest.getPhone();
            String email = loginRequest.getEmail();

            if (StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
                // 邮箱和电话号码都为空
                return ResponseResult.FAIL("请输入正确的邮箱或电话号码哦!");
            }

            // 验证账号是否合法
            boolean emailInValid = CustomValidator.invalidEmail(email);
            boolean mobileInvalid = CustomValidator.invalidMobile(phone);
            if (emailInValid && mobileInvalid) {
                // 邮箱和手机号都不正确
                return ResponseResult.FAIL("请输入正确的邮箱或电话号码哦!");
            }

            // 邮箱或手机号存在一个正确
            // 获取验证码
            String code = null;
            if (BooleanUtils.isTrue(!emailInValid)) {
                // 用户是否存在校验
                user = this.userManager.findUserByAccountOrPhoneOrEmail(email);
                // 登录结果验证
                if (Objects.isNull(user)) {
                    // 登录失败
                    return ResponseResult.FAIL("账号不存在或未绑定邮箱!");
                }
                code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + email);
            } else {
                user = this.userManager.findUserByAccountOrPhoneOrEmail(phone);
                // 登录结果验证
                if (Objects.isNull(user)) {
                    // 登录失败
                    return ResponseResult.FAIL("账号不存在或未绑定手机号!");
                }
                code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
            }

            // 校验验证码
            if (!Objects.equals(code, loginRequest.getCode())) {
                // 验证码错误
                return ResponseResult.FAIL("手机号或邮箱或验证码错误,请重试哦!");
            }

        } else {
            // 其他登录方式: 非法登录 or 第三方登录
            return ResponseResult.FAIL("目前暂不支持第三方登录!");
        }

        // 登录成功
        // 设置token
        String authToken = authorizationService.createAuthToken(user);

        // TODO 组装信息，发布事件：删除验证码，推送消息
        UserInfo userInfo = userInfoService.getById(user.getId());
        UserVO userVO = UserMapStructure.INSTANCE.toVo(user, userInfo);
        // 响应登录结果
        return ResponseResult.SUCCESS(userVO.setAccessToken(authToken));
    }

    /**
     * 注册用户: 昵称，密码，手机或邮箱，验证码
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult register(LoginOrRegisterRequest request) {

        // 参数校验
        String phone = request.getPhone();
        String email = request.getEmail();

        // 验证账号是否合法
        boolean emailInValid = CustomValidator.invalidEmail(email);
        boolean mobileInvalid = CustomValidator.invalidMobile(phone);
        if (emailInValid && mobileInvalid) {
            // 邮箱和手机号都不正确
            return ResponseResult.FAIL("请输入正确的邮箱或电话号码哦!");
        }

        // 邮箱或手机号存在一个正确
        // 校验账户是否存在
        User user = this.lambdaQuery().eq(User::getEmail, email).one();
        if (Objects.nonNull(user)) {
            // 用户已存在
            return ResponseResult.FAIL("账户已存在，请勿重复注册!");
        }

        // 获取验证码
        String code = null;
        if (BooleanUtils.isTrue(!emailInValid)) {
            // 邮箱有效
            code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + email);
        } else {
            code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        }

        // 账号不存在，注册
        // 校验验证码
        if (!Objects.equals(code, request.getCode())) {
            // 验证码错误
            return ResponseResult.FAIL("手机号或邮箱或验证码错误,请重试哦!");
        }

        // 验证码正确
        // User 用户-信息
        user = new User();
        UserInfo userInfo = new UserInfo();
        // 昵称
        String nickname = request.getNickname();
        if (StringUtils.isEmpty(nickname)) {
            nickname = NameUtil.getName();
        }
        user.setUsername(nickname);
        // 校验邮箱或手机号正确性
        if (BooleanUtils.isTrue(!emailInValid)) {
            // 邮箱有效
            user.setEmail(email);
        }
        if (BooleanUtils.isTrue(!mobileInvalid)) {
            // 手机号有效
            user.setPhone(phone);
        }
        userInfo.setNickname(user.getUsername());
        // 密码加盐加密
        String salt = BCrypt.gensalt();
        user.setPassword(BCrypt.hashpw(request.getPassword(), salt));
        // 生成账号
        user.setAccount(AccountNumberGenerator.nextAccount());
        // 注册用户成功，生成二维码
        // 生成二维码
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        QRCodeUtil.getQRCode(UserConstants.USER_QR_CODE + user.getAccount(), bos);
        String qrcode = MinioConstant.USER_QR_CODE_PATH + MinioConstant.USER_QRCODE_PREFIX + user.getAccount() + ".jpg";
        // 上传二维码
        String qrcodeUrl = this.minioService.putObject(StreamUtil.convertToInputStream(bos),
                MinioConstant.BucketEnum.User.getBucket(), qrcode);
        user.setQrcode(qrcodeUrl);

        // 注册结果
        ResponseResult result = new ResponseResult().setSuccess(false);

        // 保存用户用户信息
        Boolean res = userManager.saveUserAndInfo(user, userInfo);


        return result.setSuccess(res).setData(UserMapStructure.INSTANCE.toVo(user, userInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUserAndInfo(UserVO userVO) {

        // 生成对象
        User user = UserMapStructure.INSTANCE.toUser(userVO);
        UserInfo userInfo = UserMapStructure.INSTANCE.toUserInfo(userVO);

        // 更新用户
        boolean userUpdateRes = this.updateById(user);
        boolean uInfoUpdateRes = this.userInfoService.updateById(userInfo);

        // 响应结果
        ResponseResult result = new ResponseResult(ResultCode.FAIL);
        if (BooleanUtils.isTrue(userUpdateRes && uInfoUpdateRes)) {
            // 更新信息成功, 查询最新信息
            userInfo = this.userInfoService.getById(userInfo.getUserId());
            user = this.getById(user.getId());
            result.setResultCode(ResultCode.SUCCESS);
            result.setData(UserMapStructure.INSTANCE.toVo(user, userInfo));
        }

        return result;
    }


    /**
     * 获取用户信息
     *
     * @param id
     *
     * @return
     */
    @Override
    public ResponseResult getUserAndInfo(@NonNull Long id) {

        // 参数校验
        if (StringUtils.isEmpty(String.valueOf(id))) {
            return ResponseResult.FAIL("请您选择正确的用户哦!");
        }

        // 查询用户
        User user = this.getById(id);
        if (Objects.isNull(user)) {
            return ResponseResult.FAIL("用户不存在!");
        }

        // 组装查询用户信息
        ResponseResult result = ResponseResult.SUCCESS();
        UserVO userAndInfo = this.userManager.findUserAndInfo(id);

        result.setData(userAndInfo);

        return result;
    }


    /**
     * 批量获取用户信息
     *
     * @param idList
     *
     * @return
     */
    @Override
    public ResponseResult getUserAndInfoList(List<Long> idList) {

        // 参数校验
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseResult.SUCCESS(Collections.emptyList());
        }

        List<UserVO> userVOList = this.userManager.findUserAndInfoList(idList);
        return ResponseResult.SUCCESS(userVOList);
    }


    /**
     * 上传更新用户头像
     *
     * @param inputStream
     * @param user
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult updateUserAvatar(InputStream inputStream, User user) throws IOException {

        // 校验参数
        if (Objects.isNull(user) || StringUtils.isEmpty(String.valueOf(user.getId()))) {
            return ResponseResult.FAIL("抱歉，你还未登录!");
        }

        // 文件大小
        if (Objects.isNull(inputStream) || inputStream.available() == 0) {
            return ResponseResult.FAIL("请选择头像图片哦!");
        }

        // 构造用户头像名称: public_userid_avatar_xxx
        String bigAvatar = MinioConstant.USER_BIG_AVATAR_PREFIX + user.getId();
        String miniAvatar = MinioConstant.USER_MINI_AVATAR_PREFIX + user.getId();
        // objectName
        String bigObjectName = MinioConstant.USER_AVATAR_PATH + bigAvatar + ".jpg";
        String miniObjectName = MinioConstant.USER_AVATAR_PATH + miniAvatar + ".jpg";
        // bucket
        String bucket = MinioConstant.BucketEnum.User.getBucket();
        try {
            // 复制一份流
            ByteArrayOutputStream outputStream = StreamUtil.copyInputStream(inputStream);

            // 生成缩略图
            BufferedImage bufferedImage = ThumbnailsUtil.changeSizeToBufferedImage(
                    new ByteArrayInputStream(outputStream.toByteArray()),
                    UserConstants.MINI_AVATAR_WIDTH, UserConstants.MINI_AVATAR_HEIGHT);

            // 上传到 minio
            String bigUrl = this.minioService.putObject(new ByteArrayInputStream(outputStream.toByteArray()), bucket, bigObjectName);
            String miniUrl = this.minioService.putObject(StreamUtil.getImageStream(bufferedImage), bucket, miniObjectName);

            // 发布用户更新头像事件：更新用户信息
            user.setBigAvatar(bigUrl);
            user.setMiniAvatar(miniUrl);
            applicationEventPublisher.publishEvent(new UserUpdatedEvent(this, user));

            return ResponseResult.SUCCESS(UserMapStructure.INSTANCE.toVo(user, null));
        } catch (Exception e) {
            log.error("update user avatar error", e);
            return ResponseResult.FAIL("更新用户头像失败");
        }
    }


    /**
     * 找回密码或修改密码
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult forgetOrUpdatePassword(ForgetAccountRequest request) {

        // 参数校验
        Integer type = request.getType();
        String account = request.getAccount();
        String newPassword = request.getNewPassword();
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseResult.FAIL("输入的新密码不能为空哦!");
        }

        // 校验参数
        boolean invalidMobile = CustomValidator.invalidMobile(account);
        boolean invalidEmail = CustomValidator.invalidEmail(account);
        if (invalidEmail && invalidMobile) {
            // 非法邮箱 非法手机号 -> 为账号类型
            return ResponseResult.FAIL("请输入正确的手机号或邮箱");
        }

        Boolean res = null;
        // 根据动作类型执行操作
        if (ForgetAccountRequest.ActionTypeEnum.FORGET.ordinal() == type) {
            // 忘记密码: 需要验证码，邮箱/手机号，新密码
            User user = this.userManager.findUserByAccountOrPhoneOrEmail(request.getAccount());
            if (Objects.isNull(user)) {
                // 用户不存在
                return ResponseResult.FAIL("用户不存在或未绑定相关账号!").setMessage("用户不存在或未绑定相关账号!");
            }

            // 比较验证码
            String authCode = this.authorizationService.getAuthCode(RedisConstants.MODIFY_PASSWORD_KEY + account);
            if (!Objects.equals(authCode, request.getCode())) {
                // 验证不匹配
                return ResponseResult.FAIL("您输入的验证码错误!").setMessage("您输入的验证码错误!");
            }

            // 修改密码
            LambdaUpdateChainWrapper<User> updateWrapper = this.lambdaUpdate();
            String hashpw = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            if (BooleanUtils.isTrue(!invalidEmail)) {
                // 绑定邮箱
                res = updateWrapper.eq(User::getEmail, account)
                        .set(User::getPassword, hashpw).update();
            } else if (BooleanUtils.isTrue(!invalidMobile)) {
                res = updateWrapper.eq(User::getPhone, account)
                        .set(User::getPassword, hashpw).update();
            }

        } else if (ForgetAccountRequest.ActionTypeEnum.MODIFY.ordinal() == type) {
            // 修改密码

        }

        if (BooleanUtils.isTrue(res)) {
            // 修改密码成功
            return ResponseResult.SUCCESS("修改密码成功!").setMessage("修改密码成功!");
        }

        return ResponseResult.FAIL("修改密码失败!").setMessage("修改密码失败!");
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public ResponseResult logout(LoginOrRegisterRequest request) {

        // 发布事件，消息

        // 删除 token
        this.authorizationService.deleteAuthToken(null);

        return null;
    }


}




