package com.hk.im.admin.interceptor;


import com.hk.im.admin.util.UserContextHolder;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.JWTUtils;
import com.hk.im.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 前后端分离有时候会有两次请求，第一次为OPTIONS请求，默认会拦截所有请求，
         * 但是第一次请求又获取不到jwt，所以会出错。
         * https://www.cnblogs.com/lyh233/p/14472245.html
         */
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 获取 token
        String authorization = request.getHeader("Authorization");
        log.info("requeset interceptor:{},token={}", request.getHeaderNames(), authorization);

        // 验证是否登录
        //token不存在
        if (StringUtils.isEmpty(authorization)) {
            throw new ApiException().setMessage("对不起您还未登录或登录已过期!");
        }

        //验证token
        String userId = JWTUtils.validateToken(authorization);
        if (StringUtils.isEmpty(userId)) {
            throw new ApiException(ResultCode.TOKEN_INVALIDATE);
        }

        // 验证是否和当前用户token 一致
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        String redisToken = this.stringRedisTemplate.opsForValue().get(key);
        if (!Objects.equals(redisToken, authorization)) {
            // 不一致
            throw new ApiException(ResultCode.TOKEN_INVALIDATE);
        }

        // 刷新 token 过期时间
        this.stringRedisTemplate.expire(key, RedisConstants.ACCESS_TOKEN_TTL, TimeUnit.SECONDS);

        // 给当前 threadLocal 设置用户
        UserContextHolder.set(new User().setId(Long.valueOf(userId)));
        log.info("requeset:{},token={},user={}", request, authorization, userId);

        // 放行
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 释放资源
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 避免 postHandle 方法没有执行释放资源，再次释放
        UserContextHolder.remove();
    }

}
