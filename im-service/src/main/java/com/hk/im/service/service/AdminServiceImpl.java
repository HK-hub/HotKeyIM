package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.AdminService;
import com.hk.im.client.service.AuthorizationService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Admin;
import com.hk.im.domain.response.AdminLoginResult;
import com.hk.im.infrastructure.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 */
@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService {


    @Resource
    private AuthorizationService authorizationService;

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addAdmin(Admin admin) {

        // 参数校验
        boolean preCheck = Objects.isNull(admin) || StringUtils.isEmpty(admin.getPassword()) || StringUtils.isEmpty(admin.getUsername());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 检查管理员是否存在了
        Long count = this.lambdaQuery()
                .eq(Admin::getUsername, admin.getUsername())
                .count();
        if (count > 0) {
            // 管理员已经存在
            return ResponseResult.FAIL("管理员账号已经存在了!");
        }

        // 设置密码
        String password = admin.getPassword();
        admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        // 保存管理员
        boolean save = this.save(admin);
        if (BooleanUtils.isFalse(save)) {
            return ResponseResult.FAIL("保存管理员失败!");
        }

        return ResponseResult.SUCCESS("添加或创建管理员成功!");
    }


    /**
     * 登录管理端
     * @param admin
     * @return
     */
    @Override
    public ResponseResult login(Admin admin) {

        // 参数校验
        // 参数校验
        boolean preCheck = Objects.isNull(admin) || StringUtils.isEmpty(admin.getPassword()) || StringUtils.isEmpty(admin.getUsername());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        // 获取用户密码
        String username = admin.getUsername();
        String plainPassword = admin.getPassword();

        // 获取管理员
        Admin one = this.lambdaQuery()
                .eq(Admin::getUsername, username)
                .one();
        if (Objects.isNull(one)) {
            // 管理员不存在
            return ResponseResult.FAIL("管理员账号或密码错误!");
        }

        // 校验账号密码
        boolean checkpw = BCrypt.checkpw(plainPassword, one.getPassword());
        if (BooleanUtils.isFalse(checkpw)) {
            // 密码错误
            return ResponseResult.FAIL("管理员账号或密码错误!");
        }

        // 登录成功，创建 token
        String adminToken = this.authorizationService.createAdminToken(admin);

        return ResponseResult.SUCCESS(new AdminLoginResult().setAccessToken(adminToken));
    }
}




