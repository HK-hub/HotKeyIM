package com.hk.im.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : EditNoteArticleResponse
 * @date : 2023/3/28 16:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditNoteArticleResponse {

    // 摘要
    private String summary;

    // id
    private Long id;

    // 标题
    private String title;

    // 封面
    private String cover;

}
