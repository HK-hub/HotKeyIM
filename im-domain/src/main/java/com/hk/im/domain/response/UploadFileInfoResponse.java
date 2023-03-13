package com.hk.im.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

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
     * 文件分片大小
     */
    private Integer splitSize;

    /**
     * 已经上传了的分片索引
     */
    private List<Integer> uploadedIndex;


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

    /**
     * 是否已经存在
     */
    private Boolean exists = Boolean.FALSE;

    /**
     * 是否开启秒传
     */
    private Boolean enableTransferBySeconds = Boolean.FALSE;


    /**
     * 临时上传文件token
     */
    private String uploadToken;





}
