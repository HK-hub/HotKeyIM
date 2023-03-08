package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDate;

/**
 * @author : HK意境
 * @ClassName : ChangeUserDetailRequest
 * @date : 2023/3/8 15:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class ChangeUserDetailRequest {

    private Long userId;

    private String username;

    private String avatar;

    // 个性签名，座右铭
    private String motto;

    // birthday
    private LocalDate birthday;

    private Integer gender;

}
