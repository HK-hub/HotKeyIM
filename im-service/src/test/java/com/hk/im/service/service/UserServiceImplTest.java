package com.hk.im.service.service;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author : HK意境
 * @ClassName : UserServiceImplTest
 * @date : 2022/12/31 14:49
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class UserServiceImplTest {

    // 加密测试
    @Test
    public void testBcrypt() {

        String password = "";
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
        System.out.println(BCrypt.checkpw("",
                "$2a$10$Qubkup9bTgNtRQ9QvIHVYu4e9EOecNAnVRHOBgQ.AgomwWZyLvDuO"));
    }

}