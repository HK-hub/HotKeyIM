package com.hk.im.service.service.impl;

import com.apifan.common.random.source.NumberSource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.util.AccountNumberGenerator;
import com.hk.im.common.util.CustomValidator;
import com.hk.im.common.util.NameUtil;
import com.hk.im.domain.vo.UserVO;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.dto.LoginOrRegisterRequest;
import com.hk.im.domain.entity.User;
import com.hk.im.domain.entity.UserInfo;
import com.hk.im.infrastructure.manager.UserManager;
import com.hk.im.infrastructure.mapper.UserMapper;
import com.hk.im.infrastructure.mapstruct.UserMapStructure;
import com.hk.im.service.service.AuthorizationService;
import com.hk.im.service.service.MailService;
import com.hk.im.service.service.UserInfoService;
import com.hk.im.service.service.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
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
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 事务管理器
    @Resource
    private UserManager userManager;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AuthorizationService authorizationService;
    @Resource
    private MailService mailService;


    /**
     * 发送验证码
     * @param type 默认为登录注册
     * @param account
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
            String content = "<h2>您的登录注册验证码如下：" + code + "</h2>";
            mailService.sendSimpleMail("3161880795@qq.com", "Hot Key IM 官方",account,
                    null,"Hot Key IM聊天应用-登陆注册验证码", content);

            // 保存码
            authorizationService.createAuthCode(account,code);
            System.out.println("您的验证码已经发送了：" + code);
        } else {
            // 发送手机号码
            return ResponseResult.FAIL("暂不支持此操作!");
        }

        return ResponseResult.SUCCESS("您的验证码已经发送了!");
    }


    /**
     * 用户登录
     *
     * @param loginRequest
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

        }
        else if (Objects.equals(type, LoginOrRegisterRequest.LoginType.CODE.ordinal())) {
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

        }
        else {
            // 其他登录方式: 非法登录 or 第三方登录
            return ResponseResult.FAIL("目前暂不支持第三方登录!");
        }

        // 登录成功
        // 设置token
        String authToken = authorizationService.createAuthToken(user);

        // 组装信息，发布事件
        UserInfo userInfo = userInfoService.getById(user.getId());
        UserVO userVO = UserMapStructure.INSTANCE.toVo(user, userInfo);
        // 响应登录结果
        return ResponseResult.SUCCESS(userVO.setToken(authToken));
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


        // 获取验证码
        String code = null;
        if (BooleanUtils.isTrue(!emailInValid)) {
            // 邮箱有效
            code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + email);
            User user = this.lambdaQuery().eq(User::getEmail, email).one();
            if (Objects.nonNull(user)) {
                // 用户已存在
                return ResponseResult.FAIL("账户以存在，请勿重复注册!");
            }

        } else {
            code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
            User user = this.lambdaQuery().eq(User::getPhone, phone).one();
            if (Objects.nonNull(user)) {
                // 用户已存在
                return ResponseResult.FAIL("账户以存在，请勿重复注册!");
            }
        }

        // 账号不存在，注册
        // 校验验证码
        if (!Objects.equals(code, request.getCode())) {
            // 验证码错误
            return ResponseResult.FAIL("手机号或邮箱或验证码错误,请重试哦!");
        }

        // 验证码正确
        // User 用户-信息
        User user = new User();
        UserInfo userInfo = new UserInfo();
        // 昵称
        user.setUsername(NameUtil.getName());
        user.setPhone(phone);
        user.setEmail(email);
        userInfo.setNickname(user.getUsername());
        // 密码加盐加密
        String salt = BCrypt.gensalt();
        user.setPassword(BCrypt.hashpw(request.getPassword(), salt));
        // 生成账号
        user.setAccount(String.valueOf(AccountNumberGenerator.nextAccount()));

        // 注册结果
        ResponseResult result = new ResponseResult().setSuccess(false);

        // 保存用户用户信息
        Boolean res = userManager.saveUserAndInfo(user, userInfo);
        return result.setSuccess(res).setData(UserMapStructure.INSTANCE.toVo(user,userInfo));
    }


}




