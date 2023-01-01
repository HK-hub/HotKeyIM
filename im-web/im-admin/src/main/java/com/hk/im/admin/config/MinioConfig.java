package com.hk.im.admin.config;

import com.hk.im.admin.properties.MinioProperties;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

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

    @Resource
    private MinioProperties minioProperties;


    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient = new MinioClient.Builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return minioClient;
    }



}
