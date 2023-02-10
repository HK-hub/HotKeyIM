package com.hk.im.admin.test;

import com.hk.im.common.util.AvatarUtil;
import org.junit.jupiter.api.Test;

/**
 * @author : HK意境
 * @ClassName : UserAvatarTest
 * @date : 2023/2/10 16:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UserAvatarTest {


    /**
     * 生成随机默认头像保存在本地
     */
    @Test
    public void getPng(){
        for (int i = 0; i < 10; i++) {
            String avatar = AvatarUtil.generateAvatar();
            System.out.println(avatar);
            String bigAvatar = AvatarUtil.generateBigAvatar(200);
            System.out.println(bigAvatar);
            System.out.println(AvatarUtil.getBigAvatar(avatar));
            System.out.println("--------------------------------");
        }
    }



}
