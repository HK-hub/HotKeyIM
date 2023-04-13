package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : TagArticleRequest
 * @date : 2023/3/30 17:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class TagArticleRequest {

    private Long articleId;

    private List<Long> tags;

}
