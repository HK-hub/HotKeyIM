package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : UploadFileInfoRequest
 * @date : 2023/3/12 15:17
 * @description : 获取文件上传请求需要的文件上传信息和配置
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UploadFileInfoRequest {

    /**
     * 待上传文件名称
     */
    private String fileName;

    /**
     * 待上传文件大小: 单位byte 字节，1 byte = 8 bit
     */
    private Long size;


    /**
     * 待上传文件hash值
     */
    private String md5;

    /**
     * 文件hash
     */
    private String hash;

}
