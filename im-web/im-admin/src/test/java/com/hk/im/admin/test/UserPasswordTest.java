package com.hk.im.admin.test;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author : HK意境
 * @ClassName : UserPasswordTest
 * @date : 2023/5/28 16:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserPasswordTest {

    @Test
    public void testBcrypt() throws Exception {

        String hashpw = BCrypt.hashpw("123456hh", BCrypt.gensalt());
        System.out.println(hashpw);
    }


}
