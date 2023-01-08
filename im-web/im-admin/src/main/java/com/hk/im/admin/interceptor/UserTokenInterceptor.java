package com.hk.im.admin.interceptor;

import com.hk.im.admin.util.UserContextHolder;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.JWTUtils;
import com.hk.im.domain.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : UserTokenInterceptor
 * @date : 2023/1/7 10:22
 * @description : 验证用户是否登录
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public UserTokenInterceptor(StringRedisTemplate redisTemplate) {
        this.stringRedisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //http的header中获得token
        String token = request.getHeader(JWTUtils.USER_LOGIN_TOKEN);
        //token不存在
        if (StringUtils.isEmpty(token)) {
            throw new ApiException().setMessage("对不起您还未登录或登录已过期!");
        }
        //验证token
        String userId = JWTUtils.validateToken(token);
        if (StringUtils.isEmpty(userId)) {
            throw new ApiException(ResultCode.TOKEN_INVALIDATE);
        }


        // 验证是否和当前用户token 一致
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        String redisToken = stringRedisTemplate.opsForValue().get(key);
        if (!Objects.equals(redisToken, token)) {
            // 不一致
            throw new ApiException(ResultCode.TOKEN_INVALIDATE);
        }

        //更新token有效时间 (如果需要更新其实就是产生一个新的token)
        if (JWTUtils.isNeedUpdate(token)) {
            String newToken = JWTUtils.createToken(userId);
            stringRedisTemplate.opsForValue().set(key, token,
                    RedisConstants.ACCESS_TOKEN_TTL, TimeUnit.SECONDS);
            response.setHeader(JWTUtils.USER_LOGIN_TOKEN, newToken);
        }

        // 给当前 threadLocal 设置用户
        UserContextHolder.set(new User().setId(Long.valueOf(userId)));

        return true;
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 释放资源
        UserContextHolder.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 避免 postHandle 方法没有执行释放资源，再次释放
        UserContextHolder.remove();
    }

}
