package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : DownloadMessageFileRequest
 * @date : 2023/3/15 22:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class DownloadMessageFileRequest {

    // 下载凭证
    private String accessToken;

    // 聊天记录，消息id
    private String recordId;

    // 下载用户id
    private String userId;


}
