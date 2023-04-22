package com.hk.im.client.service;

import com.hk.im.domain.entity.Admin;
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
     * 为用户创建 accessToken
     * @param user
     * @return
     */
    public String createAuthToken(User user);

    public String createAdminToken(Admin admin);

    /**
     * 删除用户 accessToken
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
    public String createAuthCode(String type, String user, String code);

    /**
     * 获取用户的验证码
     * @param key
     * @return
     */
    public String getAuthCode(String key);

    /**
     * 认证用户
     * @param token
     * @return
     */
    public User authUserByToken(String token);


    /**
     * 获取用户在线状态
     * @param userId
     * @return
     */
    public Boolean getUserOnlineStatus(Long userId);


    /**
     * 设置用户文件上传临时授权token
     * @param uploaderId
     * @return
     */
    public String getOrSetUserUploadToken(Long uploaderId);
}
