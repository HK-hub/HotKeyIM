package com.hk.im.common.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;


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
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    // 一共多少条
    private Long totalNumbers;
    // 当前页
    private Long currentPage;
    // 一共多少页
    private Long totalPages;
    // 每页多少条
    private Long pageSize;
    // 是否有上一页
    private Boolean previous;
    // 是否有下一页
    private Boolean next;
    // 记录
    private List<T> rows;

    public static <T> PageResult<T> SUCCESS(List<T> records, long current) {

        PageResult<T> result = new PageResult<>();
        result.setCurrentPage(current);
        if (CollectionUtils.isEmpty(records)) {
            records = new ArrayList<>();
        }
        result.setPageSize((long) records.size());
        result.setRows(records);
        return result;
    }

    public static <T> PageResult<T> of(List<T> records, long current, Boolean previous, Boolean next) {

        PageResult<T> result = new PageResult<>();
        result.setCurrentPage(current);
        if (CollectionUtils.isEmpty(records)) {
            records = new ArrayList<>();
        }
        result.setPageSize((long) records.size());
        result.setRows(records);
        result.setPrevious(previous).setNext(next);
        return result;
    }


    /**
     * 流式刷新：比如 feed 流，下拉刷新
     * @param records
     * @param next
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> of(List<T> records , Boolean next) {

        PageResult<T> result = new PageResult<>();
        if (CollectionUtils.isEmpty(records)) {
            records = new ArrayList<>();
        }
        result.setPageSize((long) records.size());
        result.setRows(records);
        result.setNext(next);
        return result;
    }

}
