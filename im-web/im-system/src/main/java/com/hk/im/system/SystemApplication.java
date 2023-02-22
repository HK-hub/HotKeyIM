package com.hk.im.system;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : HK意境
 * @ClassName : SystemApplication
 * @date : 2023/2/21 22:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@EnableAdminServer
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(SystemApplication.class, args);

    }

}
