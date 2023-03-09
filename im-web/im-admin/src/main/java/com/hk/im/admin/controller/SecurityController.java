package com.hk.im.admin.controller;

import com.hk.im.client.service.RSAService;
import com.hk.im.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : SecurityController
 * @date : 2023/3/9 11:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/secure")
public class SecurityController {


    @Resource
    private RSAService rsaService;


    /**
     * 获取RSA公钥
     * @return
     */
    @RequestMapping("/rsa/public")
    public ResponseResult getRASPublicKey() {
        String publicKey = this.rsaService.generatePublicKey();
        return ResponseResult.SUCCESS(publicKey);
    }


}
