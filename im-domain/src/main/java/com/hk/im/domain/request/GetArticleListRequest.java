package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : GetArticleListRequest
 * @date : 2023/3/27 16:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GetArticleListRequest {

    private Long userId;

    // 查询第几页
    private Integer page;

    // 查询关键字
    private String keyword;

    // 查询类型
    private Integer findType;

    private Integer cid;

    // 每页查询笔记条数
    private Integer pageSize = 10;
}
