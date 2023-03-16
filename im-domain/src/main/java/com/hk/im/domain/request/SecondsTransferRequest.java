package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SecondsTransferRequest
 * @date : 2023/3/12 15:54
 * @description : 秒传文件请求
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SecondsTransferRequest {

    /**
     * 待上传文件名称
     */
    private String fileName;


    /**
     * 待上传文件hash值
     */
    private String md5;

    /**
     * 文件hash
     */
    private String hash;

    private Long receiverId;


}
