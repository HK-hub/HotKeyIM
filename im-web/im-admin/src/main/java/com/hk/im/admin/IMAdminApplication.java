package com.hk.im.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : HK意境
 * @ClassName : IMAdminApplication
 * @date : 2022/12/30 15:55
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@MapperScan("com.hk.im.infrastructure.mapper")
@SpringBootApplication(scanBasePackages = "com.hk.im")
public class IMAdminApplication {

    public static void main(String[] args) {

        SpringApplication.run(IMAdminApplication.class, args);

    }

}
