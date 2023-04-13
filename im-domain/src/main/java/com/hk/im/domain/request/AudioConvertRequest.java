package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : AudioConvertRequest
 * @date : 2023/3/18 16:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class AudioConvertRequest {

    private Long recordId;

    // 访问token
    private String token;

}
