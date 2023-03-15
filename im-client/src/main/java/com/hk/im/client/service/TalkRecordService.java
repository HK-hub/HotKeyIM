package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.DownloadMessageFileRequest;

/**
 * @author : HK意境
 * @ClassName : TalkRecordService
 * @date : 2023/3/15 22:15
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface TalkRecordService {

    /**
     * 下载聊天记录中的文件
     * @param request
     * @return
     */
    ResponseResult downloadRecordFile(DownloadMessageFileRequest request);

}
