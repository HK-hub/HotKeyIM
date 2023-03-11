package com.hk.im.admin.controller;

import com.hk.im.client.service.SplitUploadService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.SplitUploadRequest;
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


    /**
     * 检查上传文件信息，包括是否已经存在文件hash 值等，是否已经上传过
     * @param request
     * @return
     */
    @GetMapping("/file/info")
    public ResponseResult checkUploadFileInfo(SplitUploadRequest request) {



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
    public ResponseResult mergeSplitFile(SplitUploadRequest request) {

    }



}
