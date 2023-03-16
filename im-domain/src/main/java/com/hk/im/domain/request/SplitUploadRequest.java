package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : HK意境
 * @ClassName : SplitUploadRequest
 * @date : 2023/3/10 17:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SplitUploadRequest {

    /**
     * 临时文件ID
     */
    private String uploadId;

    /**
     * 当前索引块
     */
    private Integer splitIndex;

    /**
     * 总上传索引块
     */
    private Integer splitNum;

    private MultipartFile file;

    private String originalFileName;

    private String token;

}
