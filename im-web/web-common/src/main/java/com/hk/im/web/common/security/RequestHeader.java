package com.hk.im.web.common.security;

import lombok.Builder;
import lombok.Data;

/**
 * @author : HK意境
 * @ClassName : RequestHeader
 * @date : 2023/2/23 11:29
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Builder
public class RequestHeader {

    // 签名
    private String sign;
    // 请求时间
    private Long timestamp;
    // 防重用 hash 值
    private String nonce;

}
