package com.hk.im.domain.request.emoticon;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : CollectUserEmoticonRequest
 * @date : 2023/5/8 23:28
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CollectUserEmoticonRequest {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 表情包消息id
     */
    private Long recordId;

    /**
     * 表情包关键词
     */
    private String keyword;

}
