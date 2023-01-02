package com.hk.im.common.resp;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : PageResult
 * @date : 2021/11/26
 * @description : 公共分页
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private Long totalNumbers;
    private Long currentPage;
    private Long totalPages;
    private Long pageSize;
    private Long numOfElements;
    private List<T> rows;

    public static <T> PageResult<T> SUCCESS(List<T> records, long current) {

        PageResult<T> result = new PageResult<>();
        result.setCurrentPage(current);
        if (CollectionUtil.isEmpty(records)) {
            records = new ArrayList<>();
        }
        result.setPageSize((long) records.size());
        result.setRows(records);
        return result;
    }

}
