package com.hk.im.domain.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : EditNoteCategoryRequest
 * @date : 2023/3/27 16:01
 * @description : 编辑用户笔记分类
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class EditNoteCategoryRequest {

    private Long userId;

    // 操作：1.编辑，2.添加
    private Integer operation;

    private Long categoryId;

    /**
     * 笔记分类
     */
    private String name;

    /**
     * 笔记分类图标封面
     */
    private String avatar;

    /**
     * 分类说明
     */
    private String description;

}
