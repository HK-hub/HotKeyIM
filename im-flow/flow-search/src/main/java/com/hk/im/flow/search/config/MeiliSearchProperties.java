package com.hk.im.flow.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
@ConfigurationProperties(prefix = "ms")
public class MeiliSearchProperties {

    private static String default_host = "http://47.120.6.12:7700";

    // meili search 的 url 地址
    private String host;

    //默认的认证的签名key
    private String apiKey = null;


}
