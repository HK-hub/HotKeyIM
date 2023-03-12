package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.SplitUpload;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.MergeSplitFileRequest;
import com.hk.im.domain.request.SecondsTransferRequest;
import com.hk.im.domain.request.SplitUploadRequest;

/**
 * @ClassName : SplitUploadService
 * @author : HK意境
 * @date : 2023/3/10 17:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface SplitUploadService extends IService<SplitUpload> {

    /**
     * 分片上传聊天消息文件
     * @param request
     * @return
     */
    ResponseResult uploadTalkFile(SplitUploadRequest request);


    /**
     * 合并文件
     * @param request
     * @return
     */
    ResponseResult mergeSplitUploadFile(MergeSplitFileRequest request);

    /**
     * 秒传文件
     * @param request
     * @return
     */
    ResponseResult transferFileBySeconds(SecondsTransferRequest request);
}
