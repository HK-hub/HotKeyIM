package com.hk.im.flow.search.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MeiliSearchProperties
 * @date : 2023/4/16 12:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Component
public class MeiliSearchProperties {

    public static String default_host = "http://47.120.6.12:7700";

    // meili search 的 url 地址
    @Value("${ms.host}")
    private String host;

    //默认的认证的签名key
    private String apiKey = null;


}
