package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.CloudResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.UploadFileInfoRequest;

/**
 * @ClassName : CloudResourceService
 * @author : HK意境
 * @date : 2023/3/12 14:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface CloudResourceService extends IService<CloudResource> {


    /**
     * 待上传文件查询文件信息
     * @param request
     * @return
     */
    ResponseResult checkUploadFileInfo(UploadFileInfoRequest request);

    /**
     * 是否存在对应资源
     * @return {@link CloudResource}
     */
    public CloudResource existsUploadFileInfo(String fileName, String hash, String md5, Long size );

    /**
     * 增加原资源引用计数
     * @param cloudResource
     * @param by
     * @return
     */
    boolean increaseResourceCount(CloudResource cloudResource, int by);
}
