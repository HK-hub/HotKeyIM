package com.hk.im.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : TagVO
 * @date : 2023/3/28 10:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class TagVO {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标签分类
     */
    private String name;

    /**
     * 标签下文章数量
     */
    private Integer count;

    /**
     * 标签图标封面
     */
    private String avatar;

    /**
     * 标签说明
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
