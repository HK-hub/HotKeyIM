package com.hk.im.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : PreviewLinkBO
 * @date : 2023/4/14 21:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class PreviewLinkBO {

    private String url;
    private String domain;
    private String title;
    private String description;
    private String imageUrl;
}
