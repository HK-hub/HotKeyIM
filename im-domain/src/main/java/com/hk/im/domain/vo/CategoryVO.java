package com.hk.im.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : CategoryVO
 * @date : 2023/3/28 9:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class CategoryVO {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 笔记分类
     */
    private String name;

    // 分类下文章数量
    private Integer count;

    /**
     * 笔记分类图标封面
     */
    private String avatar;

    /**
     * 分类说明
     */
    private String description;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
