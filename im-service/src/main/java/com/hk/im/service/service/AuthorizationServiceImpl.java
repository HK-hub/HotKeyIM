package com.hk.im.service.service;

import cn.hutool.core.lang.UUID;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.util.JWTUtils;
import com.hk.im.domain.constant.UserConstants;
import com.hk.im.domain.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : AuthorizationServiceImpl
 * @date : 2022/12/30 21:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String createAuthToken(User user) {
        String token = JWTUtils.createToken(String.valueOf(user.getId()));
        String key = RedisConstants.LOGIN_USER_KEY + user.getId();
        stringRedisTemplate.opsForValue().set(key, token,
                RedisConstants.ACCESS_TOKEN_TTL, TimeUnit.SECONDS);
        return token;
    }


    /**
     * 删除用户 token
     * @param user
     * @return
     */
    @Override
    public String deleteAuthToken(User user) {
        String key = RedisConstants.LOGIN_USER_KEY + user.getId();
        this.stringRedisTemplate.delete(key);
        return key;
    }


    /**
     * 五分钟有效
     * @param user
     * @param code
     * @return
     */
    @Override
    public String createAuthCode(String type, String user, String code) {
        if (Objects.equals(type, UserConstants.FIND_PASSWORD)) {
            stringRedisTemplate.opsForValue().set(RedisConstants.MODIFY_PASSWORD_KEY+user, code, 10, TimeUnit.MINUTES);
        } else if (Objects.equals(type,UserConstants.LOGIN_OR_REGISTER)) {
            stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+user, code, 10, TimeUnit.MINUTES);
        } else if (Objects.equals(type,UserConstants.CHANGE_PASSWORD)) {
            stringRedisTemplate.opsForValue().set(RedisConstants.MODIFY_PASSWORD_KEY+user, code, 10, TimeUnit.MINUTES);
        }
        return code;
    }


    /**
     * 获取用户的验证码
     * @param key
     * @return
     */
    @Override
    public String getAuthCode(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    @Override
    public User authUserByToken(String token) {

        String userId = JWTUtils.validateToken(token);
        if (StringUtils.isEmpty(userId)) {
            // token 验证错误
            return null;
        }

        // 验证是否和当前用户token 一致
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        String redisToken = this.stringRedisTemplate.opsForValue().get(key);

        if (!Objects.equals(token, redisToken)) {
            // 用户token 验证错误
            return null;
        }

        return new User().setId(Long.valueOf(userId));
    }


    /**
     * 获取用户在线状态
     * @param userId
     * @return
     */
    @Override
    public Boolean getUserOnlineStatus(Long userId) {
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        // 判断  token 是否存在
        Boolean exists = this.stringRedisTemplate.hasKey(key);

        return exists;
    }


    /**
     * 获取或设置用户文件上传临时授权token
     * @param uploaderId
     * @return {@link String}
     */
    @Override
    public String getOrSetUserUploadToken(Long uploaderId) {

        String key = RedisConstants.UPLOAD_TOKEN + uploaderId;
        String token = this.stringRedisTemplate.opsForValue().get(key);
        // 获取过期时间
        Long expire = this.stringRedisTemplate.getExpire(key, TimeUnit.MINUTES);

        if (Objects.nonNull(expire) && StringUtils.isNotEmpty(token) && expire > 5L) {
            // token 存在并且有效期充足, 过期时间大于5分钟
            return token;
        }

        // token 过期，不存在，或快过期
        token = UUID.fastUUID().toString(true);
        this.stringRedisTemplate.opsForValue().set(key, token, 30, TimeUnit.MINUTES);

        return token;
    }
}
