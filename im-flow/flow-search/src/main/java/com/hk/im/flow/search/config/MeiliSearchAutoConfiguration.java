package com.hk.im.flow.search.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : MeiliSearchAutoConfiguration
 * @date : 2023/4/16 14:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class MeiliSearchAutoConfiguration {

    @Resource
    MeiliSearchProperties properties;

    @Bean
    @ConditionalOnMissingBean(Client.class)
    public Client meiliClient() {
        return new Client(config());
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config config() {
        return new Config(properties.getHost(), properties.getApiKey());
    }

}
