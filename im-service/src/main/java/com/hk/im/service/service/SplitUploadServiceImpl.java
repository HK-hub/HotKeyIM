package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.SplitUpload;
import com.hk.im.domain.request.SplitUploadRequest;
import com.hk.im.infrastructure.mapper.SplitUploadMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName : SplitUploadServiceImpl
 * @author : HK意境
 * @date : 2023/3/10 17:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class SplitUploadServiceImpl extends ServiceImpl<SplitUploadMapper, SplitUpload> implements SplitUploadService {

    /**
     * 分片上传聊天消息文件
     * @param request
     * @return
     */
    @Override
    public ResponseResult uploadTalkFile(SplitUploadRequest request) {
        return null;
    }
}




