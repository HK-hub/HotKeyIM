package com.hk.im.service.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : HK意境
 * @ClassName : MinioConfig
 * @date : 2022/12/31 23:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class MinioConfig {

    /**
     * 连接地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;
    /**
     * 用户名
     */
    @Value("${minio.accessKey}")
    private String accessKey;
    /**
     * 密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;
    /**
     * 域名
     */
    @Value("${minio.nginxHost}")
    private String nginxHost;


    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient = new MinioClient.Builder()
                .endpoint(this.endpoint)
                .credentials(this.accessKey, this.secretKey)
                .build();
        return minioClient;
    }



}
