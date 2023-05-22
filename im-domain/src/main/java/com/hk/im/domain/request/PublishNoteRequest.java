package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : PublishNoteRequest
 * @date : 2023/5/22 10:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class PublishNoteRequest {

    /**
     * 发布者
     */
    private Long userId;

    /**
     * 发布笔记
     */
    private Long noteId;

    /**
     * 计划发布时间
     */
    private LocalDateTime planTime;


    /**
     * 查看策略：0.ALL,
     */
    private Integer viewStrategy;

    /**
     * 可以查看用户集合
     */
    private List<Long> enableList;


    /**
     * 不允许查看用户集合
     */
    private List<Long> disableList;

}
