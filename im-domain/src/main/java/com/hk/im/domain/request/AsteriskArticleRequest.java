package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : AsteriskArticleRequest
 * @date : 2023/3/30 8:44
 * @description : 星标：收藏，取消收藏笔记
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class AsteriskArticleRequest {

    // 1.星标，2.取消收藏
    public Integer type;


    public Long articleId;

}
