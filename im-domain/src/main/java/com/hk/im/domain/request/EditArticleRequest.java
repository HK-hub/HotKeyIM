package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : EditArticleRequest
 * @date : 2023/3/28 14:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditArticleRequest {

    private Long userId;

    private Long noteId;

    private String title;

    // html 格式内容
    private String content;

    // md 格式内容
    private String mdContent;

    private Long categoryId;

}
