package com.hk.im.admin.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
public class MinioProperties {

    /**
     * 连接地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * console 地址
     */
    @Value("${minio.console}")
    private String console;

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
     *     //"默认存储桶"
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 图片的最大大小
     */
    @Value("${minio.imageSize}")
    private long imageSize;

    /**
     * 其他文件的最大大小
     */
    @Value("${minio.fileSize}")
    private long fileSize;


    /**
     * nginx
     */
    @Value("${minio.nginx}")
    private String nginx;




}
