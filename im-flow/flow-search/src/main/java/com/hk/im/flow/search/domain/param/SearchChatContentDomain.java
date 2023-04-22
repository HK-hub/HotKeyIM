package com.hk.im.flow.search.domain.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SearchChatContentDomain
 * @date : 2023/4/21 23:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SearchChatContentDomain {

    private Long userId;

    private String keyword;


}
