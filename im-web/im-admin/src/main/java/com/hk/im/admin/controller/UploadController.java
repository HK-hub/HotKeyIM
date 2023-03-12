package com.hk.im.admin.controller;

import com.hk.im.client.service.CloudResourceService;
import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.MergeSplitFileRequest;
import com.hk.im.domain.request.SecondsTransferRequest;
import com.hk.im.domain.request.SplitUploadRequest;
import com.hk.im.domain.request.UploadFileInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : UploadController
 * @date : 2023/3/10 17:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private SplitUploadService splitUploadService;
    @Resource
    private CloudResourceService cloudResourceService;

    /**
     * 检查上传文件信息，包括是否已经存在文件hash 值等，是否已经上传过
     * 如果已经上传完成，则进行秒传，如果有上传记录，但是没有上传完成则进行断点续传
     * @param request
     * @return
     */
    @GetMapping("/file/info")
    public ResponseResult checkUploadFileInfo(UploadFileInfoRequest request) {
        return this.cloudResourceService.checkUploadFileInfo(request);
    }


    /**
     * 文件秒传
     * @param request
     * @return
     */
    @PostMapping("/file/seconds")
    public ResponseResult fileTransferBySeconds(SecondsTransferRequest request) {
        return this.splitUploadService.transferFileBySeconds(request);
    }

    /**
     * 大文件分片上传
     * @param request
     * @return
     */
    @PostMapping("/file/split")
    public ResponseResult splitUploadTalkFile(SplitUploadRequest request) {
        return this.splitUploadService.uploadTalkFile(request);
    }


    /**
     * 合并分片上传的文件
     * @param request
     * @return
     */
    @PostMapping("/file/merge")
    public ResponseResult mergeSplitFile(MergeSplitFileRequest request) {
        return this.splitUploadService.mergeSplitUploadFile(request);
    }


}
