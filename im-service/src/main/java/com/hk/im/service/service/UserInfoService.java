package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.UserInfo;

/**
 * @ClassName : UserInfoService
 * @author : HK意境
 * @date : 2023/1/7 19:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface UserInfoService extends IService<UserInfo> {


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
   public ResponseResult getUserInfo(Long userId, String token);

}
