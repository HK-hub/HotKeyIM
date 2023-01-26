package com.hk.im.admin.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hk.im.domain.entity.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : CacheConfig
 * @date : 2023/1/26 19:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class CacheConfig {

    // 构造 Caffine 缓存 Bean 对象
    @Bean(name = "sequenceCache")
    public Cache<String, Sequence> getSequenceCache() {
        return Caffeine.newBuilder()
                // 过期时间: 一个小时
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();
    }

}
