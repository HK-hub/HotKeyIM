package com.hk.im.service.service.impl;

import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.domain.entity.User;
import com.hk.im.service.service.AuthorizationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
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
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY+user.getId(), token,
                RedisConstants.ACCESS_TOKEN_TTL, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public String deleteAuthToken(User user) {
        return null;
    }


    /**
     * 五分钟有效
     * @param user
     * @param code
     * @return
     */
    @Override
    public String createAuthCode(String user, String code) {
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+user, code, 10, TimeUnit.MINUTES);
        return code;
    }
}
