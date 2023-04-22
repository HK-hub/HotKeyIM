package com.hk.im.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : AdminLoginResult
 * @date : 2023/4/22 20:39
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class AdminLoginResult {

    private String accessToken;

}
