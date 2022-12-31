package com.hk.im.service.service;

import com.hk.im.domain.entity.User;

/**
 * @author : HK意境
 * @ClassName : AuthorizationService
 * @date : 2022/12/30 21:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface AuthorizationService {

    /**
     * 为用户创建 token
     * @param user
     * @return
     */
    public String createAuthToken(User user);

    /**
     * 删除用户 token
     * @param user
     * @return
     */
    public String deleteAuthToken(User user);

    /**
     * 用户验证码
     * @param user
     * @param code
     * @return
     */
    String createAuthCode(String user, String code);
}
