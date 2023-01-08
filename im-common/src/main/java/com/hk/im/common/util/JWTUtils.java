package com.hk.im.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hk.im.common.error.ApiException;
import com.hk.im.common.resp.ResultCode;

import java.util.Date;

/**
 * @Author: xiang
 * @Date: 2021/5/11 21:11
 * <p>
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）,默认：{"alg": "HS512","typ": "JWT"}
 * payload的格式 设置：（用户信息、创建时间、生成时间）
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 */


public class JWTUtils {

    //定义token返回头部
    public static String header = "Authorization";

    //token前缀
    public static String tokenPrefix = "Bearer ";

    //签名密钥
    public static String secret = "qwertyuiop7418520";

    //有效期: 一小时
    public static long expireTime = 3600L * 1000 * 2;

    //存进客户端的token的key名
    public static final String USER_LOGIN_TOKEN = "Authorization";

    public void setHeader(String header) {
        JWTUtils.header = header;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JWTUtils.tokenPrefix = tokenPrefix;
    }

    public void setSecret(String secret) {
        JWTUtils.secret = secret;
    }

    public void setExpireTime(int expireTimeInt) {
        JWTUtils.expireTime = expireTimeInt*1000L*60;
    }

    /**
     * 创建TOKEN
     * @param sub
     * @return
     */
    public static String createToken(String sub){

        return tokenPrefix + JWT.create()
                .withSubject(sub)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }


    /**
     * 验证token
     * @param token
     */
    public static String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    // subject 为userId
                    .getSubject();
        } catch (TokenExpiredException e){
            // throw new ApiException(ResultCode.TOKEN_ERROR);
            return null;
        } catch (Exception e){
            // throw new ApiException(ResultCode.TOKEN_ERROR);
            return null;
        }
    }

    /**
     * 检查token是否需要更新
     * @param token
     * @return
     */
    public static boolean isNeedUpdate(String token) {
        //获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e){
            return true;
        } catch (Exception e){
            return true;
            // throw new ApiException(ResultCode.TOKEN_INVALIDATE);
        }
        //如果剩余过期时间少于过期时常的一般时 需要更新
        return (expiresAt.getTime()-System.currentTimeMillis()) < (expireTime>>1);
    }


    public static void main(String[] args) {

        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODk3OTg3OTgiLCJleHAiOjE2NzMwNTc0Mjl9.HORuy-zd-cWT2umRo0bZ-zCLhVLZu_Tnle7c5JAhFsYIUiFeISI_OVnZA1nKGL_OOQBF3BfAG3ss6wxkQr6ZPg";
        System.out.println(token);

        System.out.println(validateToken(token));
        boolean needUpdate = isNeedUpdate(token);
        System.out.println(needUpdate);

    }


}

