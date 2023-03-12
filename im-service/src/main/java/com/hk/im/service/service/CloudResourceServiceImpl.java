package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CloudResourceService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.CloudResource;
import com.hk.im.domain.request.UploadFileInfoRequest;
import com.hk.im.infrastructure.mapper.CloudResourceMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName : CloudResourceServiceImpl
 * @author : HK意境
 * @date : 2023/3/12 14:52
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class CloudResourceServiceImpl extends ServiceImpl<CloudResourceMapper, CloudResource> implements CloudResourceService {

    /**
     * 待上传文件查询文件信息
     * @param request
     * @return
     */
    @Override
    public ResponseResult checkUploadFileInfo(UploadFileInfoRequest request) {
        return null;
    }
}




