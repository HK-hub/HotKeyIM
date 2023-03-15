package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : MergeSplitFileRequest
 * @date : 2023/3/12 15:46
 * @description : 合并上传请求
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class MergeSplitFileRequest {

    private String uploadId;

    private String fileName;

    private Integer size;

    private String md5;

    /**
     * 文件hash
     */
    private String hash;

    private String uploaderId;

    private String token;


}
