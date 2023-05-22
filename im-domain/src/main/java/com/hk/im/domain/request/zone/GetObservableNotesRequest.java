package com.hk.im.domain.request.zone;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : GetObservableNotesRequest
 * @date : 2023/5/22 12:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class GetObservableNotesRequest {

    private Long userId;

    /**
     * 当前页数
     */
    private Integer currentPage = 1;

    /**
     * 一页多少条
     */
    private Integer limit = 30;

}
