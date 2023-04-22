package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName : AdminService
 * @author : HK意境
 * @date : 2023/4/22 20:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface AdminService extends IService<Admin> {

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    ResponseResult addAdmin(Admin admin);

    /**
     * 登录管理后台
     * @param admin
     * @return
     */
    ResponseResult login(Admin admin);
}
