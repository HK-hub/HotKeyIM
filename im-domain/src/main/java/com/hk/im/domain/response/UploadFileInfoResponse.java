package com.hk.im.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : UploadFileInfoResponse
 * @date : 2023/3/12 15:20
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class UploadFileInfoResponse {


    /**
     * 文件上传临时id
     */
    private String uploadId;

    /**
     * 待上传文件名称
     */
    private String fileName;

    /**
     * 待上传文件大小: 单位字节 byte ，1 byte = 8 bit
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
