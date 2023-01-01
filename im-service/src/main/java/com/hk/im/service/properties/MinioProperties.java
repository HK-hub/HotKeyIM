package com.hk.im.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : HK意境
 * @ClassName : MinioProperties
 * @date : 2023/1/1 12:33
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 是一个URL，域名，IPv4或者IPv6地址")
     */
    private String endpoint;

    /**
     *     //"accessKey类似于用户ID，用于唯一标识你的账户"
     */
    private String accessKey;

    /**
     *     //"secretKey是你账户的密码"
     */
    private String secretKey;

    /**
     *     //"默认存储桶"
     */
    private String bucketName;

    /**
     * 图片的最大大小
     */
    private long imageSize;

    /**
     * 其他文件的最大大小
     */
    private long fileSize;


    /**
     * nginx
     */
    private String nginx;


}
