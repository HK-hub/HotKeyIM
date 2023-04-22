package com.hk.im.admin.controller.admin;

import com.hk.im.client.service.AdminService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : AdminController
 * @date : 2023/4/22 19:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/manage")
public class AdminController {


    @Resource
    private AdminService adminService;

    /**
     * 登录管理后台
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody Admin admin) {

        return this.adminService.login(admin);
    }


    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @PostMapping("/admin/create")
    public ResponseResult addAdmin(@RequestBody Admin admin) {

        return this.adminService.addAdmin(admin);
    }

}
